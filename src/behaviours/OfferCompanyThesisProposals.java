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
* Company agent behaviour that sends the theses proposals to student agent who wants
* to search for an EXTERNAL thesis opportunity
* */
public class OfferCompanyThesisProposals extends CyclicBehaviour {
    private List<Thesis> companyThesisList;

    public OfferCompanyThesisProposals(Agent agent, List<Thesis> companyThesisList) {
        super(agent);
        this.companyThesisList = companyThesisList;
    }

    @Override
    public void action() {

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null) {
            System.out.println("[INFO] Agent:["+myAgent.getLocalName() +"] received a request from the Agent:"+receivedMessage.getSender().getLocalName());
            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            try {
                reply.setContentObject((Serializable) companyThesisList);
            } catch (IOException e) {
                System.out.println("\n[ERROR] Agent "+ myAgent.getLocalName() +" Failed to serialize its proposalList object.");
                e.printStackTrace();
            }
            System.out.println("\n[INFO] Agent:[" + myAgent.getLocalName() + "] has sent the list of thesis proposals to Agent:[" + receivedMessage.getSender().getLocalName() + "].");
            myAgent.send(reply);

        }else {
            block();
        }

    }

}
