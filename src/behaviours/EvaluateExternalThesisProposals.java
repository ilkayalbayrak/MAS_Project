package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.ProfessorMessageContents;
import jade.core.AID;
import jade.core.Agent;
        import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;

/*
*  Thesis Committee  behaviour that evaluates external thesis proposal sent by a student
*  Every thesis has an "academical worth" value assigned to them, so the evaluation is done
* by just thresholding this value
* */
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

                     //inform the student agent that its external thesis proposal is accepted
                     ACLMessage messageToStudent = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                     messageToStudent.setConversationId(ConversationIDs.INFORM_STUDENT_IF_EXTERNAL_PROPOSAL_SUFFICIENT.toString());
                     messageToStudent.setContent(ProfessorMessageContents.EXTERNAL_THESIS_PROPOSAL_ACCEPTED.toString());
                     messageToStudent.addReceiver(receivedMessage.getSender());
                     System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informs the Agent:["+receivedMessage.getSender().getLocalName()+
                             "] that its EXTERNAL thesis proposal was academically sufficient and accepted");
                     myAgent.send(messageToStudent);

                 } else {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] says: there are no agents that providing the Service:["+receivedThesis.getThesisSubject()+"]");
                }

            }else{
                // inform the student agent that its external thesis proposal is not accepted
                ACLMessage message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                message.setConversationId(ConversationIDs.INFORM_STUDENT_IF_EXTERNAL_PROPOSAL_SUFFICIENT.toString());
                message.addReceiver(receivedMessage.getSender());
                message.setContent(ProfessorMessageContents.EXTERNAL_THESIS_PROPOSAL_REJECTED.toString());
                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] rejected the external thesis proposal of Agent:["+receivedMessage.getSender().getLocalName()+
                        "] because it was not academically sufficient");
                myAgent.send(message);
            }

        }
        else {
            block();
        }
    }
}
