package behaviours;

import agents.ThesisCommittee;
import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Utils;

import java.io.IOException;
import java.util.HashSet;

public class EvaluateExternalThesisProposals extends CyclicBehaviour {
    private final HashSet<String> acceptableThesisCourses = new HashSet<>();
    private String[] messageContent;
    private String thesisTitle = null;
    private String thesisSubject = null;


    public EvaluateExternalThesisProposals(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        acceptableThesisCourses.add("ML");
        acceptableThesisCourses.add("NLP");
        acceptableThesisCourses.add("MAS");
        acceptableThesisCourses.add("SPR");
        acceptableThesisCourses.add("LSC");

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE.name()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            System.out.println("[INFO] Agent "+myAgent.getLocalName()+" received info about EXTERNAL thesis from Agent: "+receivedMessage.getSender().getLocalName());
            try {
                messageContent = (String[]) receivedMessage.getContentObject();
                thesisTitle = messageContent[0];
                thesisSubject = messageContent[1];
                System.out.println("[INFO] EXTERNAL Thesis: " + thesisTitle+" Course: "+thesisSubject);
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent "+myAgent.getLocalName()+" could not read the contents of the message coming from Agent: "+receivedMessage.getSender().getLocalName());
                e.printStackTrace();
            }
            if (acceptableThesisCourses.contains(thesisSubject)){
                // search and select a supervisor for the chosen thesis
                // inform the student agent that its external thesis proposal is accepted
                // inform the selected supervisor
                AID[] thesisSupervisor = Utils.getAgentList(myAgent, thesisSubject);
                if(thesisSupervisor != null && thesisSupervisor.length > 0){
                    ACLMessage messageToSupervisor = new ACLMessage(ACLMessage.INFORM);
                    messageToSupervisor.setConversationId(ConversationIDs.SELECT_SUPERVISOR_FOR_EXTERNAL_THESIS.name());
                    messageToSupervisor.addReceiver(thesisSupervisor[0]);
                    try {
                        messageToSupervisor.setContentObject(messageContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    myAgent.send(messageToSupervisor);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" selected Agent: "+thesisSupervisor[0].getLocalName()+
                            " as the supervisor the Thesis: "+thesisTitle+" which will be done by Agent: "+receivedMessage.getSender().getLocalName());
                }


            }else{
                // inform the student agent that its external thesis proposal is not accepted
                ACLMessage message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                message.addReceiver(receivedMessage.getSender());
                message.setContent(StudentMessageContents.REJECT_EXTERNAL_THESIS_PROPOSAL);
                System.out.println("[INFO] Agent "+myAgent.getLocalName()+" rejected the external thesis proposal of Agent: "+receivedMessage.getSender().getLocalName());
                myAgent.send(message);
            }

        }
        else {
            block();
        }
    }
}
