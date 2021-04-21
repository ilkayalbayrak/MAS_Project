package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/*
* Supervisor behaviour that receives the rejection messages when a student
* reject supervisors thesis proposals
* This behaviour causes no action for now, but it has potential to be used in a
* creative way, therefore, it exists.
* */
public class ListenInitialProposalRejections extends CyclicBehaviour {
    public ListenInitialProposalRejections(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.REJECT_PROPOSAL_IF_STUDENT_NOT_INTERESTED.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null){
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informed by Agent:["+receivedMessage.getSender().getLocalName()+"] that the agent was not interested in any of the proposals.");
        }else {
            block();
        }

    }
}
