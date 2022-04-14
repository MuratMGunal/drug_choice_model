package drug_choice;

import simudyne.core.abm.Action;
import simudyne.core.abm.Agent;
import simudyne.core.annotations.Variable;

import java.util.Collections;
import java.util.List;

public class Drug extends Agent<DrugChoiceModel.Globals> {

    public  String Name;
    @Variable
    public  int nbActivePatients;

    @Variable
    public long EffectiveIn; //The drug cures a patient in EffectiveIn ticks

    public static Action<Drug>  AddNewPatient =
            Action.create(Drug.class, drug -> {
                drug.getMessagesOfType(Messages.StartTreatmentMessage.class).forEach(msg -> {
                    drug.addLink(msg.getSender(), Links.DrugToPatientLink.class);

                    drug.send(Messages.NewPatientMessage.class, mes -> {
                        mes.EffectiveIn = drug.EffectiveIn;
                        mes.DrugID = drug.Name;
                    }).to(msg.getSender());
                    drug.nbActivePatients ++;
                });
            });

    public static Action<Drug>  RemovePatient =
            Action.create(Drug.class, drug -> {
                drug.getMessagesOfType(Messages.RecoveredMessage.class).forEach(msg -> {
                    drug.removeLinksTo(msg.getSender());
                    drug.nbActivePatients --;
                });
            });
}
