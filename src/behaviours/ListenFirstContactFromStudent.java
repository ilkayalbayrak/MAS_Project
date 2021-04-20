package behaviours;

import agents.Reviewer;
import interfaces.ProfessorMessageContents;
import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Aulaweb;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;

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

                    // inform the student about revising the thesis
                    ACLMessage reply = receivedMessage.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(ProfessorMessageContents.REVIEWER_REVISED_THE_THESIS);
                    try {
                        reply.setContentObject(thesisToDiscuss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myAgent.send(reply);

                    AID[] thesisCommittee = Utils.getAgentList(myAgent, "thesis_committee");
                    assert thesisCommittee !=null;
                    assert thesisCommittee[0] != null;

                    //inform the thesis comm that review met the student
                    ACLMessage messageToCommittee = new ACLMessage(ACLMessage.INFORM);
                    messageToCommittee.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_REVIEWER_MET_STUDENT.toString());
                    messageToCommittee.setContent(ProfessorMessageContents.REVIEWER_INFORMS_THE_COMMITTEE_ABOUT_MEETING_THE_STUDENT);
                    messageToCommittee.addReceiver(thesisCommittee[0]);

                    try {
                        messageToCommittee.setContentObject(thesisToDiscuss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informs the Thesis Committee that they met the student Agent:["+ receivedMessage.getSender().getLocalName()+ "]");
                    myAgent.send(messageToCommittee);

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
