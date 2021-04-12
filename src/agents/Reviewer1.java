package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer1 extends Agent {
    protected void setup(){
        System.out.println("Hello Reviewer1 " + getAID().getName() + " is ready.");

        // Available thesis proposals of the supervisor

        // Register agent to yellow pages
        String[] serviceTypes = {ServiceTypes.REVIEWER.toString(), "REVIEWER_NATURAL_LANGUAGE_PROCESSING"};
        String[] serviceNames = {"professor","professor_NLP"};
        Utils.registerService(this, serviceTypes, serviceNames);
    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Reviewer1 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
