package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ChooseThesisProposal extends CyclicBehaviour {
    private HashMap<String,String> receivedProposals = new HashMap<>();
    public ChooseThesisProposal(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.name()+myAgent.getLocalName()),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null){
            System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());

            try{
                receivedProposals = (HashMap<String, String>) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent "+ myAgent.getLocalName()+ " could not extract the proposals object from message");
                e.printStackTrace();
            }
            System.out.println("\n[INFO] Reply from: "+ receivedMessage.getSender().getLocalName() + " with content: \n"  +receivedProposals);

            // randomly pick a thesis for now
            String chosenThesisTitle = Utils.pickRandomThesis(receivedProposals);
            System.out.println("\n[INFO] Agent "+myAgent.getLocalName()+ " chose the EXTERNAL thesis with the title: "+chosenThesisTitle);

            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            reply.setContent(chosenThesisTitle);
            reply.addReplyTo(receivedMessage.getSender());

            System.out.println("\n[INFO] Agent " +myAgent.getLocalName()+ " sent the chosen thesis title to its company.");
            myAgent.send(reply);


        } else {
            block();
        }

    }
}
