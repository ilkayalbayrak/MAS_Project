package behaviours;

//import interfaces.enums.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.StudentMessageContents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Utils;

public class RequestExternalThesisProposals extends OneShotBehaviour {
    public RequestExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void onStart() {
        super.onStart();
        reset();
    }

    @Override
    public void action() {
        // find the agents with the service "company"
        AID[] companyOrResearchCenters = Utils.getAgentList(myAgent, "company");
        if(companyOrResearchCenters != null && companyOrResearchCenters.length > 0){
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

            // add all agents with the company service to the message list
            for(AID company: companyOrResearchCenters){
                message.addReceiver(company);
            }

            // this message content is a place holder but it can also be used for matching this message in the receiving company agents
            message.setContent(StudentMessageContents.REQUEST_COMPANY_THESIS_PROPOSALS.toString());
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.toString());
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent a request to the companies to see their thesis opportunities.");
            myAgent.send(message);
        }else {
            System.out.println("\n[ERROR] There are no agents that offer the given service.");
        }

    }

}
