package agents;

import behaviours.EvaluateExternalThesisProposals;
import behaviours.ListenOnGoingThesisFromSupervisors;
import behaviours.ListenReviewer;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class ThesisCommittee extends Agent {
    private Thesis[] onGoingTheses;
    protected void setup(){
        System.out.println("Hello Thesis Committee " + getAID().getName() + " is ready.");
        String[]  serviceNames = {"thesis_committee"};
        String[]  serviceTypes = {"thesis_committee"};

        Utils.registerService(this, serviceTypes, serviceNames);

//        this.setGenerateBehaviourEvents();
        addBehaviour(new EvaluateExternalThesisProposals(this));
        addBehaviour(new ListenOnGoingThesisFromSupervisors(this));
        addBehaviour(new ListenReviewer(this));
    }

    protected void takeDown(){
        Utils.deregister(this);
        System.out.println("Thesis Committee " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
