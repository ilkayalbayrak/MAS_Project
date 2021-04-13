package behaviours;

import interfaces.ProfessorMessageContents;
import interfaces.StudentMessageContents;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenIfExternalProposalAccepted extends CyclicBehaviour {
    public ListenIfExternalProposalAccepted(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchContent(ProfessorMessageContents.EXTERNAL_THESIS_PROPOSAL_REJECTED),
                MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] will choose a new thesis, since the the initial one rejected because it was insufficient");
            myAgent.addBehaviour(new RequestExternalThesisProposals(myAgent));
        }else{
            block();
        }
    }
}
