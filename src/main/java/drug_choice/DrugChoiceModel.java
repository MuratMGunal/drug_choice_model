package drug_choice;

import simudyne.core.abm.AgentBasedModel;
import simudyne.core.abm.GlobalState;
import simudyne.core.abm.Group;
import simudyne.core.annotations.Input;


public class DrugChoiceModel extends AgentBasedModel<DrugChoiceModel.Globals> {

    //Globals stores all of your variables and data structures that you want your agents to be able to access
    //Store information here that is system-level knowledge (ie - # of Agents or static variables)
    public static class Globals extends GlobalState {

        //@Input public long nbHospitals = 2;
        @Input public long nbPatients = 100;
        @Input public long nbDrugs = 2;

        @Input public double InitialQoL = 1.0;

        public int nbDrugAUsers;
        public int nbDrugBUsers;

        public long DrugAiD;
        public long DrugBiD;
    }

    @Override
    public void init() {

        registerAgentTypes(Drug.class);
        registerAgentTypes(Patient.class);
        registerLinkTypes(Links.PatientToDrugLink.class, Links.DrugToPatientLink.class);
    }

    @Override
    public void setup() {

        Group<Drug> drugGroup = generateGroup(Drug.class, getGlobals().nbDrugs, agent -> {
            if (agent.getGroupPosition() == 0){
                agent.EffectiveIn = 10;
                agent.Name = "A";
                agent.getGlobals().DrugAiD = agent.getID();
            } else {
                agent.EffectiveIn = 5;
                agent.Name = "B";
                agent.getGlobals().DrugBiD = agent.getID();
            }
        });

        Group<Patient> patientGroup = generateGroup(Patient.class, getGlobals().nbPatients, agent -> {
            agent.QoL = getGlobals().InitialQoL;
            agent.DiagnosedIn = (long) agent.getPrng().uniform(0, 100).sample();
        });

        patientGroup.fullyConnected(drugGroup, Links.PatientToDrugLink.class);

        super.setup();
    }

    @Override
    public void step() {
        super.step();
        firstStep(Patient.RemoveLinks);

        run(Patient.Diagnosed, Drug.AddNewPatient, Patient.StartTreatment);
        run(Patient.Recovered, Drug.RemovePatient);
    }


}
