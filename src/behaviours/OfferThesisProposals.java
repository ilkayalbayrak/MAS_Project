package behaviours;

import interfaces.enums.StudentMessageContents;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Thesis;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class OfferThesisProposals extends CyclicBehaviour {
    private List<Thesis> proposalList;


    public OfferThesisProposals(Agent agent, List<Thesis> proposalList) {
        super(agent);
        this.proposalList = proposalList;
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null) {
            System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());
            ACLMessage reply = receivedMessage.createReply();

            // todo: remove this if cond. same reason for the todo below -->
            if (receivedMessage.getPerformative() == ACLMessage.REQUEST) {

                //todo: this if cond. can be removed because receive the message by matching the Message Content
                if (receivedMessage.getContent().equals(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS.toString())) {
                    reply.setPerformative(ACLMessage.PROPOSE);

                    try {
                        reply.setContentObject((Serializable) proposalList);
                    } catch (IOException e) {
                        System.out.println("\n[ERROR] Agent "+ myAgent.getLocalName() +" Failed to serialize its proposalList object.");
                        e.printStackTrace();
                    }
                    System.out.println("\n[INFO] Agent " + myAgent.getLocalName() + " has sent the list of thesis proposals to AGENT: {" + receivedMessage.getSender().getLocalName() + "}.");
                    myAgent.send(reply);
                }
            }
        } else {
            block();
        }
    }
}
