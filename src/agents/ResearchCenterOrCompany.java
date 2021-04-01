package agents;

import behaviours.OfferCompanyThesisProposals;
import behaviours.OfferThesisProposals;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ResearchCenterOrCompany extends Agent {
    private Map<String,String> companyThesisList = new HashMap<>();
    @Override
    protected void setup() {

        System.out.println("Hello ResearchCenterOrCompany " + getAID().getName() + " is ready.");
        //manually set the thesis
        companyThesisList.put("CompanyThesis1", "NLP");
        companyThesisList.put("CompanyThesis2", "NLP");
        companyThesisList.put("CompanyThesis3", "NLP");
        companyThesisList.put("CompanyThesis4", "NLP");
        companyThesisList.put("CompanyThesis5", "NLP");

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

    public void setProposalList(String title, String availability){
        this.companyThesisList.put(title, availability);
    }

    public Map<String, String> getProposalList(){
        return this.companyThesisList;
    }

    public void removeProposal(String title){
        this.companyThesisList.remove(title);
    }

    private class HandleThesisAcceptances extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);
            if (receivedMessage != null){

                    String chosenThesisTitle = receivedMessage.getContent();
                    removeProposal(chosenThesisTitle);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesisTitle+
                        " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");

            }else {
                block();
            }

        }
    }
}
