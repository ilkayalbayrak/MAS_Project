package agents;

import behaviours.OfferThesisProposals;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class Supervisor1 extends Agent {
    Map<String, String> proposalList = new HashMap<>();


    protected void setup(){
        System.out.println("Hello Supervisor1 " + getAID().getName() + " is ready.");
        proposalList.put("Thesis1", "available");
        proposalList.put("Thesis2", "available");
        proposalList.put("Thesis3", "available");
        proposalList.put("Thesis4", "available");
        proposalList.put("Thesis5", "available");

        // Register agent to yellow pages
        String[] serviceTypes = {"supervisor", "NLP"};
        String[] serviceNames = {"professor","professor_NLP"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
//        addBehaviour(new ListenStudents(this));
    }


    public void setProposalList(String title, String availability){
        this.proposalList.put(title, availability);
    }

    public Map<String, String> getProposalList(){
        return this.proposalList;
    }

    public void removeProposal(String title){
        this.proposalList.remove(title);
    }

    protected void takeDown(){
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

    private class HandleThesisAcceptances extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.name());
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);

            if (receivedMessage != null){
                if(receivedMessage.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){

                    String chosenThesisTitle = receivedMessage.getContent();
                    removeProposal(chosenThesisTitle);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesisTitle+
                            " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");
                } else if(receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                    System.out.println("\n[INFO] "+receivedMessage.getSender().getLocalName()+
                            " wants to reject the proposal.");
                    // todo: Send more info about the selected thesis to the student so it can decide to reject or continue with the thesis
                    // todo: if the proposal is rejected then add it back to the proposalList
                }

            }else {
                block();
            }

        }
    }

}

