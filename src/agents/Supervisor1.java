package agents;

import behaviours.HandleThesisAcceptances;
import behaviours.OfferThesisProposals;
import jade.core.Agent;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class Supervisor1 extends Agent {
    Map<String, String> proposalList = new HashMap<>();
////    private static Supervisor1 INSTANCE;
//
//    private Supervisor1(){
//
//    }
    // Singleton class pattern
    // final static could be used here
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
        addBehaviour(new HandleThesisAcceptances(this));
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

    private static void fillProposalList(){

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

}
