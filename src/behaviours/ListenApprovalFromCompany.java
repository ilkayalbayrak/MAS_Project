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

public class ListenApprovalFromCompany extends CyclicBehaviour {
    private Thesis chosenThesis;
    public ListenApprovalFromCompany(Agent agent) {
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.INFORM_STUDENT_CAN_START_COMPANY_THESIS.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if(receivedMessage != null){
            // todo: submit the thesis to the thesis committee
            System.out.println("######################################### STUDENT WAS INFORMED BY THE COMAPNY THAT IT CAN START THE THESIS");

            try {
                chosenThesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

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

                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent its chosen EXTERNAL proposal to the Agent:["+thesisCommittee[0].getLocalName()+"] for evaluation");
                myAgent.send(message);

            } else {
                System.out.println("[ERROR] There are no agents with the service that is being searched for.");
            }
        } else {
            block();
        }
    }
}
