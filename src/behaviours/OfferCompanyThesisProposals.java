package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import utils.Utils;

import java.util.List;
import java.util.Map;

public class OfferCompanyThesisProposals extends CyclicBehaviour {
    private Map<String,String> companyThesisList;
    private List<String> studentConversationIDs;

    public OfferCompanyThesisProposals(Agent agent, Map<String, String> companyThesisList) {
        super(agent);
        this.companyThesisList = companyThesisList;
    }

    @Override
    public void action() {
        AID[] studentAgents = Utils.getAgentList(myAgent,"student");
        if (studentAgents != null && studentAgents.length>0){
            for(AID student: studentAgents){
                studentConversationIDs.add(ConversationIDs.STUDENT1_ASK_COMPANY_PROPOSALS.name() + student.getLocalName());
            }
        }

//        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(studentConversationIDs.stream()
//                                                                                .filter(i -> i ==  ))

    }
}
