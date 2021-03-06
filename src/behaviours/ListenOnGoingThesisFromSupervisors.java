package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.ServiceTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Aulaweb;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;

/*
* Thesis Committee behaviour that receives messages from supervisors when a thesis
* was registered as ON GOING by the supervisor
* Then assigns a reviewer to the thesis and updates the thesis on Aulaweb adding the reviewer
* Then informs the student and the reviewer
* */
public class ListenOnGoingThesisFromSupervisors extends CyclicBehaviour {
    private Thesis onGoingRegisteredThesis;
    public ListenOnGoingThesisFromSupervisors(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_ONGOING_THESIS_REGISTRATION.toString());

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            try {
                onGoingRegisteredThesis = (Thesis) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] could not read the contents of the message received from Agent:["+receivedMessage.getSender().getLocalName()+"]");
                e.printStackTrace();
            }

            assert onGoingRegisteredThesis != null;
            assert onGoingRegisteredThesis.getThesisStudent() != null;
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] was informed by Agent:["+receivedMessage.getSender().getLocalName()+"] that the Thesis:["+onGoingRegisteredThesis.getThesisTitle()+"] which will be done by Agent:["+onGoingRegisteredThesis.getThesisStudent().getLocalName()+"] registered as ON_GOING");

            // todo: choose a reviewer for the thesis
            StringBuilder service = new StringBuilder().append(ServiceTypes.REVIEWER).append("_").append(onGoingRegisteredThesis.getThesisSubject());
            AID[] reviewer = Utils.getAgentList(myAgent, service.toString());

            // thesis comm informs the student and the supervisor and the reviewer that the reviewer will revise the thesis
            // and send a list of future deadlines
            assert reviewer != null && reviewer[0] != null;
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] choose Agent:["+reviewer[0].getLocalName()+
                    "] as the reviewer of the Thesis:["+onGoingRegisteredThesis.getThesisTitle()+"] which will be done by Agent:["+onGoingRegisteredThesis.getThesisStudent().getLocalName()+"]");

            // assign the reviewer to the thesis
            onGoingRegisteredThesis.setThesisReviewer(reviewer[0]);

            // Update the ONGOING THESES list ON AULAWEB after adding a REVIEWER for the thesis
            Aulaweb aulaweb = Aulaweb.getInstance();
            // onGoingRegisteredThesis is a slightly modified version(Added a Reviewer on the previous lines) of a Thesis obj that should be already exist in Aulaweb
            // now we are only updating it to the Reviewer added version
            aulaweb.updateOnGoingThesesByStudent(onGoingRegisteredThesis.getThesisStudent(), onGoingRegisteredThesis);

            // inform reviewer about the thesis
            ACLMessage messageToReviewer = new ACLMessage(ACLMessage.INFORM);
            messageToReviewer.addReceiver(reviewer[0]);
            messageToReviewer.setConversationId(ConversationIDs.ASSIGN_REVIEWER_FOR_THESIS.toString());
            try {
                messageToReviewer.setContentObject(onGoingRegisteredThesis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informed the reviewer Agent:["+reviewer[0].getLocalName()+
                    "] that it was assigned as reviewer for the Thesis:["+onGoingRegisteredThesis.getThesisTitle()+"]");
            myAgent.send(messageToReviewer);

            // inform student who is the reviewer
            ACLMessage messageToStudent = new ACLMessage(ACLMessage.INFORM);
            messageToStudent.setConversationId(ConversationIDs.WHO_IS_YOUR_THESIS_REVIEWER.toString());
            messageToStudent.addReceiver(onGoingRegisteredThesis.getThesisStudent());
            //
            //Do I need to send anything ?? other than the name of the reviewer
            //
            try {
                messageToStudent.setContentObject(reviewer[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAgent.send(messageToStudent);

            // inform the supervisor of the thesis about which agent will be the reviewer
            // todo: Listen for this message on supervisor but unless you implement the rest of the graduation process schema it's unnecessary
            ACLMessage messageToSupervisor = new ACLMessage(ACLMessage.INFORM);
            messageToSupervisor.setConversationId(ConversationIDs.WHO_IS_YOUR_THESIS_REVIEWER.toString());
            messageToSupervisor.addReceiver(onGoingRegisteredThesis.getThesisSupervisor());
            try {
                messageToSupervisor.setContentObject(onGoingRegisteredThesis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAgent.send(messageToSupervisor);

        }else {
            block();
        }
    }
}
