package behaviours;

import interfaces.StudentMessageContents;
import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RequestThesisProposals extends Behaviour {
    private List<Thesis> receivedProposals = new LinkedList<>();
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
                    message.setConversationId(ConversationIDs.ASK_PROPOSALS.name());
                    message.setReplyWith("message"+System.currentTimeMillis());
                    myAgent.send(message);
                    listReceivedCount = allSupervisors.length;
                    System.out.println("\n[INFO] "+myAgent.getLocalName() +" sent request to supervisors " +message.getContent());

                    messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_PROPOSALS.name()),
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
                            receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }

                        System.out.println("\n[INFO] Reply from: "+ receivedMessage.getSender().getLocalName() + " with content: \n"  +receivedProposals);

                        // randomly pick a thesis for now
                        Thesis chosenThesis = Utils.pickRandomThesis(receivedProposals);
                        System.out.println("\n[INFO] Agent "+myAgent.getLocalName()+ " chose the thesis with the title: "+chosenThesis.getThesisTitle());
                        ACLMessage reply = receivedMessage.createReply();
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.name());
                        try {
                            reply.setContentObject(chosenThesisTitle);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("\n[INFO] Agent " +myAgent.getLocalName()+ " sent the chosen thesis proposal with the Title:"+chosenThesis.getThesisTitle()+" to Agent:"+receivedMessage.getSender().getLocalName()+".");
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
