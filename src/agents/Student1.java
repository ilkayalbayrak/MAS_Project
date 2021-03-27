package agents;

import behaviours.RequestThesisProposals;
import jade.core.AID;
import jade.core.Agent;
import utils.Utils;

public class Student1 extends Agent {
    private AID[] supervisors;
    private String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed

    protected void setup(){
        System.out.println("Hello Student1 " + getAID().getName() + " is ready.");

        // TODO: Student examines the proposals
        // Ask for all proppsals
        // Student chooses the TYPE of the Thesis
        // if EXTERNAL: student prepares a TH with a company
        //      And submits the TH to the Committee for evaluation
        // if PROPOSED: Student asks for all the proposals and chooses one among them
        //      Student contact the Supervisor of the selected TH to get more info
        //      After the extra info STUDENT INFORMS SUPERVISOR if they gonna continue with the project or not
        // if AD-HOC: Student chooses a SUPERVISOR
        //      Student Chooses a SUPERVISOR
        //      STUDENT contacts the SUPERVISOR about a possible TH

        //todo: get thesis type in the beginning
        Object[] args = getArguments();
        if (args != null && args.length>0){
            thesisType = (String) args[0];
            System.out.println(getAID().getName() +": choose the thesis-type: "+thesisType);
        }

        // todo: register the agent to the yellow pages
        // Register agent to yellow pages
        String[] serviceTypes = {"student"};
        String[] serviceNames = {"student"};
        Utils.registerService(this, serviceTypes, serviceNames);

        // todo: examine the proposals
        addBehaviour(new RequestThesisProposals(this, thesisType));

        // todo: search supervisors in yellow pages


//        addBehaviour(new StudentBehaviour(this));
    }

    @Override
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

}
