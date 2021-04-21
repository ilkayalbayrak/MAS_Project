package agents;

import behaviours.*;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

import java.util.Collections;

public abstract class Student extends Agent {
    AID[] supervisors;
    String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed
    String researchInterest;
    final String[] serviceTypes= {"student"};
    String[] serviceNames;
    Thesis adHocThesis;

    @Override
    protected void setup() {
        System.out.println("Hello "+getAID().getLocalName()+ " " + getAID().getName() + " is ready.");

        Object[] args = getArguments();
        if (args != null && args.length >0){
            thesisType = (String) args[0];
            researchInterest = (String) args[1];
        }

        init();
        // Register agent to yellow pages
        Utils.registerService(this, serviceTypes, serviceNames);

        executeChosenThesisPath(this, thesisType, researchInterest, adHocThesis);
    }

    // initialize the parameters serviceNames and adHocThesis
    protected abstract void init();

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

    public static void executeChosenThesisPath(Agent agent, String thesisType, String researchInterest, Thesis adhocThesis){
        if (agent != null && thesisType != null){
            switch (thesisType){
                case "EXTERNAL":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the EXTERNAL TH path ");
                    // then ask Thesis committee if that is acceptable
                    agent.addBehaviour(new RequestExternalThesisProposals(agent));
                    agent.addBehaviour(new ChooseCompanyThesisProposal(agent));
                    agent.addBehaviour(new ListenApprovalFromCompany(agent));
                    agent.addBehaviour(new HandleChosenCompanyThesisNotExist(agent));
                    agent.addBehaviour(new ListenIfExternalProposalSufficient(agent));
                    agent.addBehaviour(new ListenWhoIsReviewer(agent));
                    agent.addBehaviour(new StartWritingThesis(agent));
                    break;
                case "AD_HOC":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the AD_HOC TH path ");
                    //choose supervisor and contact supervisor about a possible thesis
                    // Student offers its own idea for a thesis to supervisor
                    // student and supervisor come to an agreement whether the thesis is OK or not
                    if (adhocThesis !=null){
                        agent.addBehaviour(new ChooseAndContactSupervisor(agent, researchInterest, adhocThesis));
                        agent.addBehaviour(new ListenResponseForAdHocThesis(agent, adhocThesis));
                        agent.addBehaviour(new ListenWhoIsReviewer(agent));
                        agent.addBehaviour(new StartWritingThesis(agent));


                    } else {
                        System.out.println("[ERROR] Agent "+agent.getLocalName()+": There are no presented AD-HOC thesis proposals.");
                    }

                    break;
                case "PROPOSED":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the PROPOSED TH path ");
                    agent.addBehaviour(new RequestThesisProposals(agent, thesisType));
                    agent.addBehaviour(new ChooseUniThesisProposals(agent));
                    agent.addBehaviour(new HandleChosenThesisNotExist(agent));
                    agent.addBehaviour(new ListenWhoIsReviewer(agent));
                    agent.addBehaviour(new StartWritingThesis(agent));


//                    agent.addBehaviour(new Test(agent, thesisType));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value for thesisType: " + thesisType);
            }
        }else {
            assert agent != null;
            System.out.println("\n[ERROR] thesisType parameter of agent "+agent.getLocalName()+" is null");
        }

    }
}
