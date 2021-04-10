package behaviours;

import agents.ThesisCommittee;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;

public class ListenOnGoingThesisFromSupervisors extends CyclicBehaviour {
    private Thesis onGoingRegisteredThesis;
    public ListenOnGoingThesisFromSupervisors(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_ONGOING_THESIS_REGISTRATION.toString());

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            try {
                onGoingRegisteredThesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] could not read the contents of the message received from Agent:["+receivedMessage.getSender().getLocalName()+"]");
                e.printStackTrace();
            }

            assert onGoingRegisteredThesis.getThesisStudent() != null;
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] was informed by Agent:["+receivedMessage.getSender().getLocalName()+"] that the Thesis:["+onGoingRegisteredThesis.getThesisTitle()+"] which will be done by Agent:["+onGoingRegisteredThesis.getThesisStudent().getLocalName()+"] registered as ON_GOING");

            // todo: CHOOSE A REVIEWER FOR THE THESIS
        }else {
            block();
        }
    }
}
