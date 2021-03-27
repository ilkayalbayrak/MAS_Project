package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import agents.Supervisor1;

public class HandleThesisAcceptances extends CyclicBehaviour {

    public HandleThesisAcceptances(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if(receivedMessage != null){
            String chosenThesisTitle = receivedMessage.getContent();
//            Supervisor1 supervisor1 = Supervisor1.getInstance();

//            supervisor1.removeProposal(chosenThesisTitle);
//            System.out.println("\n[INFO] Updated proppsallist: " + supervisor1.getProposalList());


        }

    }
}
