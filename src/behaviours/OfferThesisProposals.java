package behaviours;

import interfaces.StudentMessageContents;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


public class OfferThesisProposals extends CyclicBehaviour {
    private Map<String,String> proposalList;


    public OfferThesisProposals(Agent agent, Map<String,String> proposalList) {
        super(agent);
        this.proposalList = proposalList;
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS);
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null) {
            System.out.println("[INFO] "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());
            ACLMessage reply = receivedMessage.createReply();
            if (receivedMessage.getPerformative() == ACLMessage.REQUEST) {
                if (receivedMessage.getContent().equals(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS)) {
                    reply.setPerformative(ACLMessage.PROPOSE);

                    try {
                        reply.setContentObject((Serializable) proposalList);
                    } catch (IOException e) {
                        System.out.println("\n[ERROR] Failed to serialize the proposalList object.");
                        e.printStackTrace();
                    }
                    System.out.println("\n[INFO] " + myAgent.getLocalName() + " says: I am sending the list of thesis proposals to AGENT: {" + receivedMessage.getSender().getLocalName() + "}.\nProposals: "+reply.getContent() +"\n");
                    myAgent.send(reply);
                }
            }
        } else {
            block();
        }
    }
}
