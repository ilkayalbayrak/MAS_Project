package behaviours;

import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ListenWhoIsReviewer extends CyclicBehaviour {
    private AID reviewer;
    public ListenWhoIsReviewer(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.WHO_IS_YOUR_THESIS_REVIEWER.toString());

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null ){
            // get the reviewer's name
            try {
                reviewer = (AID) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] contacts its thesis reviewer Agent:["+reviewer.getLocalName()+
                    "] to discuss the progress of the work" );
            ACLMessage messageToReviewer = new ACLMessage(ACLMessage.REQUEST);
            messageToReviewer.setConversationId(ConversationIDs.CONTACT_THESIS_REVIEWER.toString());
            messageToReviewer.setContent(StudentMessageContents.FIRST_CONNECTION_WITH_REVIEVER);
            messageToReviewer.addReceiver(reviewer);
            myAgent.send(messageToReviewer);

        }else {
            block();
        }
    }
}
