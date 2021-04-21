package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.ProfessorMessageContents;
import interfaces.enums.StudentMessageContents;
import jade.core.AID;
import jade.core.Agent;
        import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Aulaweb;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;

/*
* Reviewer behaviour that waits for a student to start a thesis discussion
* after the thesis and supervisor of the thesis are all decided
* */
public class ListenFirstContactFromStudent extends CyclicBehaviour {
    private Thesis thesisToDiscuss;
    public ListenFirstContactFromStudent(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.CONTACT_THESIS_REVIEWER.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if (receivedMessage.getContent().equals(StudentMessageContents.FIRST_CONNECTION_WITH_REVIEWER.toString())){
                Aulaweb aulaweb = Aulaweb.getInstance();
                thesisToDiscuss = aulaweb.getONGOING_THESES().get(receivedMessage.getSender());

                // check if the thesis academically sufficient and check if revised by the supervisor
                // academic worth threshold may be increased to start a different discussion ??
                if (thesisToDiscuss.isRevisedBySupervisor()){
                    thesisToDiscuss.setRevisedByReviewer(true);
                    aulaweb.updateONGOING_THESES(receivedMessage.getSender(), thesisToDiscuss);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received a thesis discussion request from Agent:["+ receivedMessage.getSender().getLocalName()+
                            "] and performed some revisions on the Thesis:["+thesisToDiscuss.getThesisTitle()+"]");

                    // inform the student about revising the thesis
                    ACLMessage reply = receivedMessage.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(ProfessorMessageContents.REVIEWER_REVISED_THE_THESIS.toString());
                    reply.setConversationId(ConversationIDs.START_WRITING_THE_THESIS.toString());
                    try {
                        reply.setContentObject(thesisToDiscuss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myAgent.send(reply);

                    // find thesis committee agent
                    AID[] thesisCommittee = Utils.getAgentList(myAgent, "thesis_committee");
                    assert thesisCommittee !=null;
                    assert thesisCommittee[0] != null;

                    //inform the Thesis Committee that reviewer met the student
                    ACLMessage messageToCommittee = new ACLMessage(ACLMessage.INFORM);
                    messageToCommittee.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_REVIEWER_MET_STUDENT.toString());
                    messageToCommittee.setContent(ProfessorMessageContents.REVIEWER_INFORMS_THE_COMMITTEE_ABOUT_MEETING_THE_STUDENT.toString());
                    messageToCommittee.addReceiver(thesisCommittee[0]);

                    try {
                        messageToCommittee.setContentObject(thesisToDiscuss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informs the Thesis Committee Agent:["+thesisCommittee[0].getLocalName()+
                            "] that they had met the student Agent:["+thesisToDiscuss.getThesisStudent().getLocalName()+"], and discussed the Thesis:["+thesisToDiscuss.getThesisTitle()+"]");
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
