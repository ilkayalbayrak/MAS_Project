package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenResponseForAdHocThesis extends CyclicBehaviour {
    public ListenResponseForAdHocThesis(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+myAgent.getLocalName());

//        MessageTemplate rejectionMT = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+myAgent.getLocalName()),
//                MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if (receivedMessage.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                System.out.println("[INFO] AD-HOC PROPOSAL of Agent:"+myAgent.getLocalName()+" ACCEPTED  *************************************************");

            } else if (receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                System.out.println("[INFO] AD-HOC PROPOSAL of Agent:"+myAgent.getLocalName()+" REJECTED  *************************************************");
            }
        }else {
            block();
        }


    }
}
