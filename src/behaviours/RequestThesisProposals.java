package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.StudentMessageContents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Utils;

/*
 * Student behaviour that is for asking supervisor agents for their "PROPOSED" proposals
 * */
public class RequestThesisProposals extends OneShotBehaviour {
    private final String thesisType;

    public RequestThesisProposals(Agent agent, String thesisType) {
        super(agent);
        this.thesisType = thesisType;
    }

    @Override
    public void action() {

        // find the agents with the service "supervisor"
        AID[] allSupervisors = Utils.getAgentList(myAgent, "supervisor");
        if (allSupervisors != null && allSupervisors.length > 0) {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

            // add all agents with the supervisor service to the message list
            for (AID supervisor : allSupervisors) {
                message.addReceiver(supervisor);
            }

            // this message content is a place holder but it can also be used for matching this message in the receiving supervisor agents
            message.setContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS.toString());
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.ASK_SUPERVISOR_PROPOSALS.name());
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent a request to all supervisors to see their thesis opportunities.");
            myAgent.send(message);

        } else {
            System.out.println("\n[ERROR] There are no agents that offer the given service.");
        }
    }
}
