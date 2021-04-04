package agents;

import behaviours.OfferThesisProposals;
import interfaces.SupervisorMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.util.*;


public class Supervisor1 extends Agent {
    private Map<String, String> proposalList = new HashMap<>();
    private Map<AID, Thesis> onGoingThesesList = new HashMap<>();

    public Map<AID, Thesis> getOnGoingThesesList() {
        return onGoingThesesList;
    }

    public void setOnGoingTheses(Thesis thesis, AID student) {
        onGoingThesesList.put(student, thesis);
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


    protected void setup(){
        System.out.println("Hello Supervisor1 " + getAID().getName() + " is ready.");
        proposalList.put("Thesis1", "NLP");
        proposalList.put("Thesis2", "NLP");
        proposalList.put("Thesis3", "NLP");
        proposalList.put("Thesis4", "NLP");
        proposalList.put("Thesis5", "NLP");

        // Register agent to yellow pages
        String[] serviceTypes = {"supervisor", "NLP"};
        String[] serviceNames = {"professor","professor_NLP"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
        addBehaviour(new ListenAdHocProposals(this));
//        addBehaviour(new ListenStudents(this));
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

    private class ListenAdHocProposals extends CyclicBehaviour {
        private Thesis receivedAdHocThesis;

        public ListenAdHocProposals(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.name()),
                    MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);

            if (receivedMessage != null){
                System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());
                try {
                    receivedAdHocThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent: "+myAgent.getLocalName()+" could not read the contents of the message received from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                ACLMessage reply = receivedMessage.createReply();
                if (receivedAdHocThesis.getAcademicWorth() > 50) {
                    setOnGoingTheses(receivedAdHocThesis, receivedMessage.getSender());
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(SupervisorMessageContents.AD_HOC_THESIS_PROPOSAL_ACCEPTED);
                    myAgent.send(reply);
                    System.out.println("[INFO] Agent: "+myAgent.getLocalName()+" accepted AD-HOC thesis proposal of Agent: "+
                            receivedMessage.getSender().getLocalName());
                } else {

                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(SupervisorMessageContents.AD_HOC_THESIS_PROPOSAL_REJECTED);
                    myAgent.send(reply);
                    System.out.println("[INFO] Agent: "+myAgent.getLocalName()+" rejected AD-HOC thesis proposal of Agent: "+
                            receivedMessage.getSender().getLocalName());
                }
            }else {
                block();
            }
        }
    }

}

