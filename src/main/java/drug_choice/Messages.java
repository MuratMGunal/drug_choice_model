package drug_choice;

import simudyne.core.graph.Message;

public class Messages {
    public static class StartTreatmentMessage extends Message {}
    public static class NewPatientMessage extends Message {
        public  long EffectiveIn;
        public  String DrugID;
    }
    public static class RecoveredMessage extends Message {

    }
}
