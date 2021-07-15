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

/*
* A Student behaviour to choose a thesis among all the sent proposals by the Supervisors
* */
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
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_SUPERVISOR_PROPOSALS.name()),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));

        // receive all proposals/refusals from the supervisor agents
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if(receivedMessage.getPerformative() == ACLMessage.PROPOSE){
                try {
                    receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent:["+ myAgent.getLocalName()+ "] could not extract the proposals object from message");
                    e.printStackTrace();
                }

                // store proposals coming from supervisors in a Map object
                proposalsBySupervisors.put(receivedMessage.getSender(),receivedProposals);

                // Check if all active supervisors sent their proposal lists
                if (numberOfSupervisors == proposalsBySupervisors.size()){

                    // Print out a msg when all supervisors sent their proposals
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received a reply from all supervisors that contains thesis proposals.");

                    // todo: Fix random picking by creating a gui
                    //randomly pick a supervisor for now
                    List<AID> keysAsArray = new ArrayList<>(proposalsBySupervisors.keySet());
                    Random r = new Random();
                    AID thesisSupervisor = keysAsArray.get(r.nextInt(keysAsArray.size()));

                    // randomly pick a thesis that is sent by randomly picked supervisor for now
                    Thesis chosenThesis = Utils.pickRandomThesis(proposalsBySupervisors.get(thesisSupervisor));
                    assert chosenThesis != null;

                    // set the thesisStudent variable of the thesis object to show, who owns the thesis
                    chosenThesis.setThesisStudent(myAgent.getAID());
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+ "] chose the Thesis:["+chosenThesis.getThesisTitle() + "] within all received proposals.");

                    // inform the supervisor of the picked thesis about which thesis agent wants to pick
                    ACLMessage acceptanceMessage = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    acceptanceMessage.addReceiver(thesisSupervisor);
                    acceptanceMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptanceMessage.setConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.toString());
                    try {
                        acceptanceMessage.setContentObject(chosenThesis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("[INFO] Agent:[" +myAgent.getLocalName()+ "] sent the CHOSEN THESIS PROPOSAL:["+chosenThesis.getThesisTitle()+"] to Agent:"+ thesisSupervisor.getLocalName()+".");
                    myAgent.send(acceptanceMessage);

                    // after picking one proposal from a supervisor, send rejection messages to other supervisors to inform them
                    ACLMessage rejectionMessage = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    rejectionMessage.setConversationId(ConversationIDs.REJECT_PROPOSAL_IF_STUDENT_NOT_INTERESTED.toString());

                    // Add all the rejected supervisors as receiver
                    for(AID supervisor: proposalsBySupervisors.keySet()){
                        if(!supervisor.equals(thesisSupervisor)){
                            rejectionMessage.addReceiver(supervisor);
                        }
                    }
                    System.out.println("[INFO] Agent:[" +myAgent.getLocalName()+ "] informed rest of the supervisors that it is not interested in their proposals");
                    myAgent.send(rejectionMessage);
                }
            }

        } else {
            block();
        }
    }
}
