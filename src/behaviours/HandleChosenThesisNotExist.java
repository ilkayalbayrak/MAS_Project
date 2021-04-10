package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HandleChosenThesisNotExist extends CyclicBehaviour {
    private List<Thesis> receivedProposals = new ArrayList<>();

    public HandleChosenThesisNotExist(Agent agent){
        super(agent);

    }
    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.THESIS_ALREADY_BEEN_PICKED.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if(receivedMessage != null){
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informed that the thesis proposal it chose already been chosen by another agent.");
            try {
                receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            assert receivedProposals != null;
            Thesis chosenThesis = Utils.pickRandomThesis(receivedProposals);
            assert chosenThesis != null;
            chosenThesis.setThesisStudent(myAgent.getAID());
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] picked a new proposal since the first one was taken by another agent. New Thesis:["+chosenThesis.getThesisTitle()+"]");

            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            reply.setConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.toString());
            try {
                reply.setContentObject(chosenThesis);
            } catch (IOException e) {
                System.out.println("\n[ERROR] Agent:["+ myAgent.getLocalName() +"] Failed to serialize its proposalList object.");
                e.printStackTrace();
            }
            myAgent.send(reply);

        }else {
            block();
        }

    }
}
