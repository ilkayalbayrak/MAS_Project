package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ChooseUniThesisProposals extends CyclicBehaviour {
    private Map<AID, List<Thesis>> proposalsBySupervisors = new HashMap<>();
    private List<Thesis> receivedProposals = new LinkedList<>();

    private final int numberOfSupervisors;

    public ChooseUniThesisProposals(Agent agent){
        super(agent);
        this.numberOfSupervisors = Utils.getAgentList(myAgent, "supervisor").length;
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_PROPOSALS.name()),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        // receive all proposals/refusals from the supervisor agents
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if(receivedMessage.getPerformative() == ACLMessage.PROPOSE){
                try {
                    receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

                //
                proposalsBySupervisors.put(receivedMessage.getSender(),receivedProposals);
                System.out.println("\n[INFO] Agent:"+myAgent.getLocalName()+" received a reply from Agent:"+ receivedMessage.getSender().getLocalName() + " that contains thesis proposals.");

                // Check if all active supervisors sent their proposal lists
                if (numberOfSupervisors == proposalsBySupervisors.size()){
                    // todo: Fix random picking by creating a gui
                    //randomly pick a supervisor for now
                    List<AID> keysAsArray = new ArrayList<>(proposalsBySupervisors.keySet());
                    Random r = new Random();
//                    proposalsBySupervisors.get(keysAsArray.get(r.nextInt(keysAsArray.size())));
                    AID thesisSupervisor = keysAsArray.get(r.nextInt(keysAsArray.size()));
                    // randomly pick a thesis for now
                    Thesis chosenThesis = Utils.pickRandomThesis(proposalsBySupervisors.get(thesisSupervisor));

                    assert chosenThesis != null;
                    // set the thesisStudent variable of the thesis object to show, who owns the thesis
                    chosenThesis.setThesisStudent(myAgent.getAID());
                    System.out.println("\n[INFO] Agent:["+myAgent.getLocalName()+ "] chose the Thesis:["+chosenThesis.getThesisTitle() + "] within all received proposals.");
                    ACLMessage reply = receivedMessage.createReply();
                    reply.addReceiver(thesisSupervisor);
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.toString());
                    try {
                        reply.setContentObject(chosenThesis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n[INFO] Agent:[" +myAgent.getLocalName()+ "] sent the chosen thesis proposal with the Title:["+chosenThesis.getThesisTitle()+"] to Agent:"+receivedMessage.getSender().getLocalName()+".");
                    myAgent.send(reply);

                    // todo: after picking one proposal from a supervisor, send rejection messages to other supervisors to inform them

                    ACLMessage message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    message.setConversationId(ConversationIDs.REJECT_PROPOSAL_IF_NOT_INTERESTED.toString());

                    // Add all the rejected supervisors as receiver
                    for(AID supervisor: proposalsBySupervisors.keySet()){
                        if(!supervisor.equals(thesisSupervisor)){
                            message.addReceiver(supervisor);
                        }
                    }
                    myAgent.send(message);

                } else {
                    System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" is still waiting for all supervisors to send their proposals ");
                }

            }

        } else {
            block();
        }
    }
}
