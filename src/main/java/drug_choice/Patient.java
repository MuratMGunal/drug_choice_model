package drug_choice;

import simudyne.core.abm.Action;
import simudyne.core.abm.Agent;
import simudyne.core.annotations.Variable;
import simudyne.core.graph.Link;
import simudyne.core.graph.Message;

import java.util.Collections;
import java.util.List;

public class Patient extends Agent<DrugChoiceModel.Globals> {

    @Variable
    public double QoL; //This patient's Quality of Life figure. It is between 0 and 1.

    @Variable
    public long DiagnosedIn;

    @Variable
    public String OnDrug;

    public long RecoveredTick;

    public boolean HasRecovered;

    public static Action<Patient> Diagnosed =
            Action.create(Patient.class, patient -> {

                if (!patient.HasRecovered){
                    long drugChosen = 0;

                    if (patient.DiagnosedIn == patient.getContext().getTick()) {
                        int nbDrugAUsers = patient.getGlobals().nbDrugAUsers;
                        int nbDrugBUsers = patient.getGlobals().nbDrugBUsers;

                        if (nbDrugAUsers > nbDrugBUsers){
                            drugChosen = patient.getGlobals().DrugAiD;
                        } else if (nbDrugBUsers > nbDrugAUsers){
                            drugChosen = patient.getGlobals().DrugBiD;
                        } else {
                            drugChosen = patient.getPrng().uniform(0,1).sample() > 0.5 ?
                                    patient.getGlobals().DrugAiD :
                                    patient.getGlobals().DrugBiD;
                        }
                    }
                    patient.send(Messages.StartTreatmentMessage.class).to(drugChosen);
                }
            });

    public static Action<Patient> RemoveLinks =
            Action.create(Patient.class, patient -> {
                patient.getLinks(Links.PatientToDrugLink.class).forEach(Link::remove);
            });

    public static Action<Patient> StartTreatment =
            Action.create(Patient.class, patient -> {
                Messages.NewPatientMessage NewDrugMsg = patient.getMessageOfType(Messages.NewPatientMessage.class);
                patient.addLink(NewDrugMsg.getSender(), Links.PatientToDrugLink.class);
                patient.RecoveredTick = NewDrugMsg.EffectiveIn + patient.getContext().getTick();
                patient.OnDrug = NewDrugMsg.DrugID;
            });

    public static Action<Patient> Recovered =
            Action.create(Patient.class, patient -> {
                if (patient.RecoveredTick == patient.getContext().getTick()){
                    patient.HasRecovered = true;
                    patient.getLinks(Links.PatientToDrugLink.class).forEach(link -> {
                        patient.send(Messages.RecoveredMessage.class).to(link.getTo());
                        //patient.removeLinks();
                    });
                    patient.removeLinks();
                }
            });
}
