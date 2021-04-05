package agents;

import behaviours.OfferCompanyThesisProposals;
import behaviours.OfferThesisProposals;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisMainSubjects;
import interfaces.enums.ThesisTypes;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResearchCenterOrCompany extends Agent {
    private List<Thesis> companyThesisList = new LinkedList<>();

    public void initThesisProposals(){
        // Available thesis proposals of the supervisor
        Thesis thesis1 = new Thesis();
        thesis1.setThesisSupervisor(this.getAID());
        thesis1.setThesisStudent(null);
        thesis1.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis1.setThesisTitle("NLP_Thesis1");
        thesis1.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis1.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis1.setAcademicWorth(90);

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis2.setThesisTitle("NLP_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis2.setAcademicWorth(100);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis3.setThesisTitle("NLP_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis3.setAcademicWorth(75);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis4.setThesisTitle("NLP_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis4.setAcademicWorth(87);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis5.setThesisTitle("NLP_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis5.setAcademicWorth(80);

        companyThesisList.add(thesis1);
        companyThesisList.add(thesis2);
        companyThesisList.add(thesis3);
        companyThesisList.add(thesis4);
        companyThesisList.add(thesis5);
    }

    @Override
    protected void setup() {

        System.out.println("Hello ResearchCenterOrCompany " + getAID().getName() + " is ready.");

        initThesisProposals();
        // Register agent to yellow pages
        String[] serviceTypes = {"research_center", "company"};
        String[] serviceNames = {"research_center", "company"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferCompanyThesisProposals(this, companyThesisList));
        addBehaviour(new HandleThesisAcceptances());
    }

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getLocalName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

    public void setProposalList(Thesis thesis){
        this.companyThesisList.add(thesis);
    }

    public List<Thesis> getProposalList(){
        return this.companyThesisList;
    }

    public void removeProposal(Thesis chosenThesis){

        this.companyThesisList.remove(chosenThesis);
    }

    private class HandleThesisAcceptances extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);
            if (receivedMessage != null){

                Thesis chosenThesisTitle = null;
                try {
                    chosenThesisTitle = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                removeProposal(chosenThesisTitle);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesisTitle+
                        " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");

            }else {
                block();
            }

        }
    }
}
