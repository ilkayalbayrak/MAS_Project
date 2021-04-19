package behaviours;

import agents.ThesisCommittee;
import interfaces.ProfessorMessageContents;
import interfaces.StudentMessageContents;
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
import java.util.HashSet;

public class EvaluateExternalThesisProposals extends CyclicBehaviour {
    private Thesis receivedThesis;

    public EvaluateExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_EXTERNAL_THESIS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received an EXTERNAL thesis proposal from Agent:["+receivedMessage.getSender().getLocalName()+"] for evaluation");
            try {
                receivedThesis = (Thesis) receivedMessage.getContentObject();
//                System.out.println("[INFO] Received EXTERNAL Thesis:[" + receivedThesis.getThesisTitle()+"] Course:["+receivedThesis.getThesisSubject()+"]");
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent "+myAgent.getLocalName()+" could not read the contents of the message coming from Agent: "+receivedMessage.getSender().getLocalName());
                e.printStackTrace();
            }
            assert receivedThesis != null;
             if (receivedThesis.getAcademicWorth() > 50){


                System.out.println("[INFO] Agent "+myAgent.getLocalName()+" accepted the external thesis proposal of Agent:["+receivedMessage.getSender().getLocalName()+
                        "], since the proposal was academically sufficient");

                 // search and select a supervisor for the chosen thesis
                 AID[] thesisSupervisor = Utils.getAgentList(myAgent, receivedThesis.getThesisSubject().toString());
                 // inform the selected supervisor
                 if(thesisSupervisor != null && thesisSupervisor.length > 0){

                     // set the supervisor of thesis object
                     receivedThesis.setThesisSupervisor(thesisSupervisor[0]);

                    ACLMessage messageToSupervisor = new ACLMessage(ACLMessage.INFORM);
                    messageToSupervisor.setConversationId(ConversationIDs.SELECT_SUPERVISOR_FOR_EXTERNAL_THESIS.toString());
                    messageToSupervisor.addReceiver(thesisSupervisor[0]);
                    try {
                        messageToSupervisor.setContentObject(receivedThesis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myAgent.send(messageToSupervisor);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] selected Agent:["+thesisSupervisor[0].getLocalName()+
                            "] as the supervisor the Thesis:["+receivedThesis.getThesisTitle()+"] which will be done by Agent:["+receivedMessage.getSender().getLocalName()+"]");

                     // todo: inform the student agent that its external thesis proposal is accepted

                 } else {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] says: there are no agents that providing the Service:["+receivedThesis.getThesisSubject()+"]");
                }


            }else{
                // inform the student agent that its external thesis proposal is not accepted
                ACLMessage message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                message.addReceiver(receivedMessage.getSender());
                message.setContent(ProfessorMessageContents.EXTERNAL_THESIS_PROPOSAL_REJECTED);
                System.out.println("[INFO] Agent "+myAgent.getLocalName()+" rejected the external thesis proposal of Agent:["+receivedMessage.getSender().getLocalName()+
                        "] because if was not academically sufficient");
                myAgent.send(message);
            }

        }
        else {
            block();
        }
    }
}
