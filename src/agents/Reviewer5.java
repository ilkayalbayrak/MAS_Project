package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer5 extends Agent {
    protected void setup(){
        System.out.println("Hello Reviewer5 " + getAID().getName() + " is ready.");

        // Available thesis proposals of the supervisor

        // Register agent to yellow pages
        String[] serviceTypes = {ServiceTypes.REVIEWER.toString(), "REVIEWER_SPEECH_PROCESSING_AND_RECOGNITION"};
        String[] serviceNames = {"professor","professor_SPR"};
        Utils.registerService(this, serviceTypes, serviceNames);
    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Reviewer4 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
