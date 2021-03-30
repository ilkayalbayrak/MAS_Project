package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Utils;

import java.util.Map;

public class FindExternalThesisProposals extends OneShotBehaviour {
    private Map<String,String> companyProposalList;
    public FindExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        AID[] companyOrResearchCenters = Utils.getAgentList(myAgent, "company");
        if(companyOrResearchCenters != null && companyOrResearchCenters.length > 0){
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            for(AID company: companyOrResearchCenters){
                message.addReceiver(company);
                System.out.println("\n[INFO] Company or Research Center's name: "+company.getLocalName());
            }
            message.setContent("[INFO] " + myAgent.getLocalName() + " wants to learn about possible thesis opportunities in your company.");
            message.setPerformative(ACLMessage.REQUEST);
            message.setConversationId(ConversationIDs.STUDENT1_ASK_COMPANY_PROPOSALS.name()+myAgent.getLocalName());
            myAgent.send(message);
            System.out.println("[INFO] "+myAgent.getLocalName()+" sent a request to the companies to see their thesis opportunities.");
        }

    }

}
