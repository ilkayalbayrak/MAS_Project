package behaviours;

import interfaces.StudentMessageContents;
import interfaces.enums.ConversationTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class RequestThesisProposals extends Behaviour {
    private HashMap<String, String> receivedProposals = new HashMap<>();
    private String chosenThesisTitle;
    private final String thesisType;
    private MessageTemplate messageTemplate;
    private int step,  listReceivedCount = 0;

    public RequestThesisProposals(Agent agent, String thesisType) {
        super(agent);
        this.thesisType = thesisType;

    }

    @Override
    public void action(){
        AID[] allSupervisors = Utils.getAgentList(myAgent,"supervisor");

        switch (step){
            case 0:
                if (allSupervisors != null && allSupervisors.length > 0){

                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                    for(AID supervisor: allSupervisors){
                        message.addReceiver(supervisor);
                        System.out.println("\n[INFO] Supervisor name: "+supervisor.getLocalName());
                    }
                    message.setContent(StudentMessageContents.REQUEST_ALL_THESIS_PROPOSALS);
                    message.setPerformative(ACLMessage.REQUEST);
                    message.setConversationId(ConversationTypes.ASK_PROPOSALS.name());
                    message.setReplyWith("message"+System.currentTimeMillis());
                    myAgent.send(message);
                    listReceivedCount = allSupervisors.length;
                    System.out.println("\n[INFO] "+myAgent.getLocalName() +" sent request to supervisors " +message.getContent());

                    messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationTypes.ASK_PROPOSALS.name()),
                            MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
                    step = 1;
                } else {
                    System.out.println("\n[INFO] There are no agents that offer given service.");
                    step = 2;
                }
                break;

            case 1:
                // receive all proposals/refusals from the supervisor agents
                ACLMessage receivedMessage = myAgent.receive(messageTemplate);
                if (receivedMessage != null){
                    if(receivedMessage.getPerformative() == ACLMessage.PROPOSE){
                        try {
                            receivedProposals = (HashMap) receivedMessage.getContentObject();
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }

                        System.out.println("\n[INFO] Reply from: "+ receivedMessage.getSender().getLocalName() + " with content: \n"  +receivedProposals);

                        // randomly pick a thesis for now
                        chosenThesisTitle = Utils.pickRandomThesis(receivedProposals);
                        System.out.println("\n[INFO] Student1 chose the thesis with the title: "+chosenThesisTitle);
                        ACLMessage reply = receivedMessage.createReply();
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent(chosenThesisTitle);

                        System.out.println("\n[INFO] Student1 sent the chosen thesis title to its supervisor.");
                        myAgent.send(reply);
                        step = 2;
                    }

                } else {
                    block();
                }
                break;
            default:
                step = 2;

        }
    }

    @Override
    public boolean done() {
        return (step == 2);
    }
}
