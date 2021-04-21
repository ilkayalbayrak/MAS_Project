package behaviours;

import agents.ThesisCommittee;
//import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ProfessorMessageContents;
import jade.core.Agent;
import jade.core.CallbackInvokator;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;

public class ListenReviewer extends CyclicBehaviour {
    private Thesis thesis;
    public ListenReviewer(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_REVIEWER_MET_STUDENT.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null) {

            try {
                thesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] CONFIRMS that the reviewer Agent:["+receivedMessage.getSender().getLocalName()+
                    "] had a meeting with student Agent:["+thesis.getThesisStudent().getLocalName()+"], and discussed the Thesis:["+thesis.getThesisTitle()+"]");
            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.CONFIRM);
            reply.setContent(ProfessorMessageContents.THCOMMITTEE_CONFIRMS_THE_MEETING_BETWEEN_REVIEWER_AND_STUDENT.toString());
            myAgent.send(reply);

        }else {
            block();
        }
    }
}
