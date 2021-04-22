package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Aulaweb;
import utils.Thesis;
/*
* Reviewer agent behaviour that listens messages for the info of which thesis
* the agent was assigned as a reviewer, coming from Thesis Committee agent
* After learning about the thesis to review, the agent revises the thesis,
* and pushes the updates to the Aulaweb
* */
public class ListenWhichThesisToReview extends CyclicBehaviour {
    private Thesis assignedThesis;
    public ListenWhichThesisToReview(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.ASSIGN_REVIEWER_FOR_THESIS.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){

            try {
                assignedThesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            assert assignedThesis != null;
            // revise the thesis and update it on Aulaweb
            assignedThesis.setRevisedByReviewer(true);
            Aulaweb aulaweb = Aulaweb.getInstance();
            aulaweb.updateOnGoingThesesByStudent(assignedThesis.getThesisStudent(), assignedThesis);
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] revised the Thesis:["+assignedThesis.getThesisTitle()+"] and pushed the updates to the Aulaweb");

        } else {
            block();
        }
    }
}
