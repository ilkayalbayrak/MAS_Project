package agents;

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
//        Object[] args = getArguments();
//        if (args != null && args.length>0){
//            thesisType = (String) args[0];
//            System.out.println(getAID().getName() +": choose the thesis-type: "+thesisType);
//        }

        thesisType = Utils.getThesisTypeArgument(this);

        // todo: register the agent to the yellow pages
        // Register agent to yellow pages
        String[] serviceNames = {"student1"};
        String[] serviceTypes = {"student"};
        Utils.registerService(this, serviceTypes, serviceNames);

        // todo: examine the proposals
//        if(thesisType != null){
//            switch (thesisType){
//                case "EXTERNAL":
//                    System.out.println("[INFO] Agent"+ this.getLocalName() + " chose the EXTERNAL TH path ");
//                    // todo: contact company or research center for their possible TH material projects
//                    // then ask Thesis committee if that is acceptable
//                    addBehaviour(new FindExternalThesisProposals(this));
//                    break;
//                case "AD_HOC":
//                    System.out.println("[INFO] Agent"+ this.getLocalName() + " chose the AD_HOC TH path ");
//                    //
//                    break;
//                case "PROPOSED":
//                    System.out.println("[INFO] Agent"+ this.getLocalName() + " chose the PROPOSED TH path ");
//                    addBehaviour(new RequestThesisProposals(this, thesisType));
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value for thesisType: " + thesisType);
//            }
//        } else {
//            System.out.println("\n[ERROR] thesisType parameter of agent "+this.getLocalName()+" is null");
//        }
        Utils.executeChosenThesisPath(this, thesisType);



        // todo: search supervisors in yellow pages


//        addBehaviour(new StudentBehaviour(this));
    }

    @Override
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }


}
