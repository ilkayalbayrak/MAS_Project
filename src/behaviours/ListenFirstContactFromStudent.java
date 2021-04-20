package behaviours;

import agents.Reviewer;
import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Aulaweb;
import utils.Thesis;

public class ListenFirstContactFromStudent extends CyclicBehaviour {
    private Thesis thesisToDiscuss;
    public ListenFirstContactFromStudent(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.CONTACT_THESIS_REVIEWER.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if (receivedMessage.getContent().equals(StudentMessageContents.FIRST_CONNECTION_WITH_REVIEVER)){
                Aulaweb aulaweb = Aulaweb.getInstance();
                thesisToDiscuss = aulaweb.getONGOING_THESES().get(receivedMessage.getSender());
                // check if the thesis academically sufficient and check if revised by the supervisor
                // academic worth threshold may be increased to start a different discussion
                if (thesisToDiscuss.isRevisedBySupervisor()){
                    thesisToDiscuss.setRevisedByReviewer(true);
                    aulaweb.updateONGOING_THESES(receivedMessage.getSender(), thesisToDiscuss);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received a thesis discussion request from Agent:["+ receivedMessage.getSender().getLocalName()+
                            "] and performed some revisions on the Thesis:["+thesisToDiscuss.getThesisTitle()+"]");
                }else if (thesisToDiscuss.getAcademicWorth() < 50){
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] found the Thesis:["+thesisToDiscuss+"] academically insufficient");
                } else {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] found out the Thesis:["+thesisToDiscuss+"] was not revised by the Supervisor");
                }

            }
        }else{
            block();
        }
    }
}
