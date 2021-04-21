package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.StudentMessageContents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Utils;

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
            message.setContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS.toString());
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.ASK_PROPOSALS.name());
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent a request to all supervisors to see their thesis opportunities.");
            myAgent.send(message);

        } else {
            System.out.println("\n[ERROR] There are no agents that offer the given service.");
        }
    }
}
