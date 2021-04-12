package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer2 extends Agent {
    protected void setup(){
        System.out.println("Hello Reviewer2 " + getAID().getName() + " is ready.");

        // Register agent to yellow pages
        String[] serviceTypes = {ServiceTypes.REVIEWER.toString(), "REVIEWER_MACHINE_LEARNING"};
        String[] serviceNames = {"professor","professor_ML"};
        Utils.registerService(this, serviceTypes, serviceNames);
    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Reviewer2 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
