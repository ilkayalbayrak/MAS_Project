package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChooseExternalThesisProposal extends CyclicBehaviour {
    private List<Thesis> receivedProposals = new LinkedList<>();
    public ChooseExternalThesisProposal(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.name()+myAgent.getLocalName()),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null){

            try{
                receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent "+ myAgent.getLocalName()+ " could not extract the proposals object from message");
                e.printStackTrace();
            }

            System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" received a reply from Agent:"+ receivedMessage.getSender().getLocalName() + " that contains EXTERNAL thesis proposals.");
            // randomly pick a thesis for now
            Thesis chosenThesis = Utils.pickRandomThesis(receivedProposals);

            // register the student agent to the thesis object
            assert chosenThesis != null;
            chosenThesis.setThesisStudent(myAgent.getAID());
            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            try {
                reply.setContentObject(chosenThesis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            reply.addReplyTo(receivedMessage.getSender());
//            receivedProposals.get(chosenThesisTitle);

            System.out.println("\n[INFO] Agent:["+myAgent.getLocalName()+ "] chose the EXTERNAL Thesis:["+chosenThesis.getThesisTitle()+"] and informed the collaborating company about its decision.");

            myAgent.send(reply);

            // todo: inform the thesis committee about chosen external thesis

            AID[] thesisCommittee = Utils.getAgentList(myAgent,"thesis_committee");
            if (thesisCommittee != null && thesisCommittee.length > 0){
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_EXTERNAL_THESIS.toString());
                message.addReceiver(thesisCommittee[0]);
                try {
                    message.setContentObject(chosenThesis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myAgent.send(message);

            } else {
                System.out.println("[ERROR] There are no agents with the service that is being searched for.");
            }
        } else {
            block();
        }

    }
}
