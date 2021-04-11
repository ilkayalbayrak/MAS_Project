package behaviours;

import agents.Supervisor1;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenInitialProposalRejections extends CyclicBehaviour {
    public ListenInitialProposalRejections(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.REJECT_PROPOSAL_IF_NOT_INTERESTED.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null){
            System.out.println("[INFO] Agent:["+receivedMessage.getSender().getLocalName()+"] informed Agent:["+myAgent.getLocalName()+"] that it is not interested in any of the proposals.");
        }else {
            block();
        }

    }
}
