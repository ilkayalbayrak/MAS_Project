package agents;

import behaviours.EvaluateExternalThesisProposals;
import jade.core.Agent;
import utils.Utils;

public class ThesisCommittee extends Agent {
    protected void setup(){
        System.out.println("Hello Thesis Committee " + getAID().getName() + " is ready.");
        String[]  serviceNames = {"thesis_committee"};
        String[]  serviceTypes = {"thesis_committee"};

        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new EvaluateExternalThesisProposals(this));
    }



    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Thesis Committee " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
