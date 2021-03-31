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
//    private static Supervisor1 INSTANCE;
//
//    private Supervisor1(){
//
//    }
////     Singleton class pattern
////     final static could be used here
//    public static Supervisor1 getInstance() {
//        if(INSTANCE == null){
//            INSTANCE = new Supervisor1();
//        }
//        return INSTANCE;
//    }


    protected void setup(){
        System.out.println("Hello Supervisor1 " + getAID().getName() + " is ready.");
        proposalList.put("Thesis1", "available");
        proposalList.put("Thesis2", "available");
        proposalList.put("Thesis3", "available");
        proposalList.put("Thesis4", "available");
        proposalList.put("Thesis5", "available");

        // Register agent to yellow pages
        String[] serviceTypes = {"supervisor"};
        String[] serviceNames = {"supervisor"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
//        addBehaviour(new ListenStudents(this));
    }

//    class ListenStudents extends CyclicBehaviour{
//        public ListenStudents(Agent agent){
//            super(agent);
//        }
//        @Override
//        public void action() {
//            ACLMessage msg = myAgent.receive();
//            if (msg != null){
//                // Process received message
//                String msgContent = msg.getContent();
//                ACLMessage reply = msg.createReply();
//                reply.setPerformative(ACLMessage.PROPOSE);
//                reply.setContent("These are my proposals: X,c,f,g,h,y,r,d,w,r,g,r...");
//                myAgent.send(reply);
////                System.out.println(reply);
//            } else {
//                // if there is no msg block the behaviour
//                block();
//            }
//        }
//    }


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
                    System.out.println("\n[INFO] Proposal list before removaal\n");
                    for(String title: proposalList.keySet()){
                        System.out.println("\nThesis title: "+title);
                    }
                    removeProposal(chosenThesisTitle);
                    System.out.println("\n[INFO] Proposal list after removaal\n");
                    for(String title: proposalList.keySet()){
                        System.out.println("\nThesis title: "+title);
                    }
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

