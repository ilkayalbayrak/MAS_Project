package behaviours;

import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RequestThesisProposals extends OneShotBehaviour {
    private final String thesisType;

    public RequestThesisProposals(Agent agent, String thesisType) {
        super(agent);
        this.thesisType = thesisType;
    }

    @Override
    public void action() {
        AID[] allSupervisors = Utils.getAgentList(myAgent, "supervisor");
        if (allSupervisors != null && allSupervisors.length > 0) {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            for (AID supervisor : allSupervisors) {
                message.addReceiver(supervisor);
//                System.out.println("\n[INFO] Supervisor name: " + supervisor.getLocalName());
            }
            message.setContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS);
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.ASK_PROPOSALS.name());
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent a request to all supervisors to see their thesis opportunities.");
            myAgent.send(message);

        } else {
            System.out.println("\n[ERROR] There are no agents that offer the given service.");
        }
    }
}
