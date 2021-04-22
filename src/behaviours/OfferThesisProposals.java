package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Thesis;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/*
 * Supervisor agent behaviour that sends the theses proposals to student agent who wants
 * to go for the PROPOSED PATH for a thesis opportunity
 * */
public class OfferThesisProposals extends CyclicBehaviour {
    private List<Thesis> proposalList;


    public OfferThesisProposals(Agent agent, List<Thesis> proposalList) {
        super(agent);
        this.proposalList = proposalList;
    }

    @Override
    public void action() {
//        MessageTemplate messageTemplate = MessageTemplate.MatchContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS.toString());
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_SUPERVISOR_PROPOSALS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null) {
            System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());

            if (receivedMessage.getPerformative() == ACLMessage.REQUEST) {
                ACLMessage reply = receivedMessage.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                try {
                    reply.setContentObject((Serializable) proposalList);
                } catch (IOException e) {
                    System.out.println("\n[ERROR] Agent:["+ myAgent.getLocalName() +"] Failed to serialize its proposalList object.");
                    e.printStackTrace();
                }
                System.out.println("\n[INFO] Agent:[" + myAgent.getLocalName() + "] has sent the list of thesis proposals to Agent:[" + receivedMessage.getSender().getLocalName() + "].");
                myAgent.send(reply);
            } else {
                System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] received a request see the thesis proposals but with the WRONG MSG PERFORMATIVE");
            }
        } else {
            block();
        }
    }
}
