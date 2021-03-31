package behaviours;

import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Utils;

import java.util.Map;

public class RequestExternalThesisProposals extends OneShotBehaviour {
    public RequestExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        AID[] companyOrResearchCenters = Utils.getAgentList(myAgent, "company");
        if(companyOrResearchCenters != null && companyOrResearchCenters.length > 0){
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            for(AID company: companyOrResearchCenters){
                message.addReceiver(company);
                System.out.println("\n[INFO] Company name who offers thesis proposals: "+company.getLocalName());
            }
            message.setContent(StudentMessageContents.REQUEST_COMPANY_THESIS_PROPOSALS);
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.name()+myAgent.getLocalName());
            System.out.println("[INFO] Agent "+myAgent.getLocalName()+" sent a request to the companies to see their thesis opportunities.");
            myAgent.send(message);
        }

    }

}
