package behaviours;

//import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;

public class StartWritingThesis extends CyclicBehaviour {
    private Thesis discussedThesis;
    public StartWritingThesis(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.START_WRITING_THE_THESIS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            try {
                discussedThesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] and Agent:["+receivedMessage.getSender().getLocalName()+
                    "] finalized their discussion about the Thesis:["+discussedThesis.getThesisTitle()+"], the student will start writing the thesis");

        }else {
            block();
        }

    }
}
