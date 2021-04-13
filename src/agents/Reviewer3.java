package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer3 extends Agent {
    protected void setup(){
        System.out.println("Hello Reviewer3 " + getAID().getName() + " is ready.");

        // Available thesis proposals of the supervisor

        // Register agent to yellow pages
        String[] serviceTypes = {ServiceTypes.REVIEWER.toString(), "REVIEWER_MULTI_AGENT_SYSTEMS"};
        String[] serviceNames = {"professor","professor_MAS"};
        Utils.registerService(this, serviceTypes, serviceNames);
    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Reviewer3 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
