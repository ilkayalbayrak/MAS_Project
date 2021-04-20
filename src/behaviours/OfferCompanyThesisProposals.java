package behaviours;

import agents.Supervisor1;
import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OfferCompanyThesisProposals extends CyclicBehaviour {
    private List<Thesis> companyThesisList;
    private List<String> studentConversationIDs;

    public OfferCompanyThesisProposals(Agent agent, List<Thesis> companyThesisList) {
        super(agent);
        this.companyThesisList = companyThesisList;
    }

    @Override
    public void action() {
//        AID[] studentAgents = Utils.getAgentList(myAgent,"student");
//        if (studentAgents != null && studentAgents.length>0){
//            for(AID student: studentAgents){
//                studentConversationIDs.add(ConversationIDs.ASK_COMPANY_PROPOSALS.name() + student.getLocalName());
//            }
//        }


        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null) {
            System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());
            ACLMessage reply = receivedMessage.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
//            Supervisor1.
            try {
                reply.setContentObject((Serializable) companyThesisList);
            } catch (IOException e) {
                System.out.println("\n[ERROR] Agent "+ myAgent.getLocalName() +" Failed to serialize its proposalList object.");
                e.printStackTrace();
            }
            System.out.println("\n[INFO] Agent " + myAgent.getLocalName() + " has sent the list of thesis proposals to AGENT: {" + receivedMessage.getSender().getLocalName() + "}.");
            myAgent.send(reply);

        }else {
            block();
        }

    }

}
