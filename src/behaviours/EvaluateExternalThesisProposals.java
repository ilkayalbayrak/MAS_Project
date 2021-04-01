package behaviours;

import agents.ThesisCommittee;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class EvaluateExternalThesisProposals extends CyclicBehaviour {
    public EvaluateExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE.name()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            System.out.println("[INFO] Agent "+myAgent.getLocalName()+" received info about EXTERNAL thesis from Agent: "+receivedMessage.getSender().getLocalName());
            String[] externalThesisTopic;
            try {
                externalThesisTopic = (String[]) receivedMessage.getContentObject();
                System.out.println("[INFO] EXTERNAL Thesis: " + externalThesisTopic[0]+" Course: "+externalThesisTopic[1]);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        else {
            block();
        }
    }
}
