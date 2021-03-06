package agents;

import behaviours.ListenInitialProposalRejections;
import behaviours.OfferThesisProposals;
//import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ProfessorMessageContents;
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
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Supervisor extends Agent {
    static List<Thesis> proposalList = new LinkedList<>();

    // personal ongoing theses list of supervisor, this may be unnecessary
    //since now we use Aulaweb to save ONGOING theses

    static Map<AID, Thesis> onGoingThesesList = new HashMap<>();
    String[] serviceTypes;
    String[] serviceNames;

    @Override
    protected void setup() {
        System.out.println("Hello "+getAID().getLocalName()+ " " + getAID().getName() + " is ready.");

        initThesisProposals();
        init();
        Utils.registerService(this, serviceTypes, serviceNames);

        // add supervisor behaviours
        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
        addBehaviour(new ListenInitialProposalRejections(this));
        addBehaviour(new ListenAdHocProposals(this));
        addBehaviour(new ListenThesisCommitteeForExternalThesis(this));
    }

    // initialize the theses proposal list of the agent
    protected abstract void initThesisProposals();

    // initialize serviceNames, serviceTypes and
    protected abstract void init();

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }


////////////////////////////////////////////////// ----- NESTED BEHAVIOURS ----- //////////////////////////////////////////////////

    private static class HandleThesisAcceptances extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.ACCEPT_THESIS_PROPOSAL.name());
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);

            if (receivedMessage != null){
                if(receivedMessage.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){

                    Thesis chosenThesis = null;
                    try {
                        chosenThesis = (Thesis) receivedMessage.getContentObject();
                    } catch (UnreadableException e) {
                        System.out.println("[ERROR] Agent ["+ myAgent.getLocalName()+ "] could not extract the proposals object from message");
                        e.printStackTrace();
                    }

                    assert chosenThesis != null;

                    // get instance of Aulaweb class to get ONGOING THESES
                    Aulaweb aulaweb = Aulaweb.getInstance();

                    // todo: Check if the chosen thesis is already in the ON GOING theses list

                    // if the chosen title already been picked, send the AVAILABLE proposals so the agent can pick another thesis proposal
                    if (aulaweb.getOnGoingThesesByStudent().containsValue(chosenThesis)){
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] says: Thesis:["+chosenThesis.getThesisTitle()+"] has already been chosen by another agent and informs the Agent:["+receivedMessage.getSender().getLocalName()+"] about the situation.");

                        ACLMessage reply = receivedMessage.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setConversationId(ConversationIDs.THESIS_ALREADY_BEEN_PICKED.toString());

                        try {
                            reply.setContentObject((Serializable) proposalList);
                        } catch (IOException e) {
                            System.out.println("\n[ERROR] Agent:["+ myAgent.getLocalName() +"] Failed to serialize its proposalList object.");
                            e.printStackTrace();
                        }

                        myAgent.send(reply);

                    } else{
                        // if the chosen thesis has not been already picked by another student agent
                        // add the thesis on the Aulaweb ONGOING THESES list
                        // Then remove the chosen thesis from the personal proposal offerings of the supervisor agent
                        proposalList.remove(chosenThesis);
                        System.out.println("[INFO] Agent: ["+myAgent.getLocalName()+"] removed the Thesis topic: ["+ chosenThesis.getThesisTitle()+
                                "] chosen by Agent: ["+ receivedMessage.getSender().getLocalName()+ "] from its available thesis proposals list.");

                        AID student = chosenThesis.getThesisStudent();
                        aulaweb.addOnGoingThesesByStudent(student,chosenThesis);
                        System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" revised the Thesis:["+chosenThesis.getThesisTitle()+"] of Agent:["+student.getLocalName()+"], and set it to ON_GOING");

                        AID[] thesisCommittee = Utils.getAgentList(myAgent,"thesis_committee");
                        if (thesisCommittee != null && thesisCommittee.length > 0){
                            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                            message.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_ONGOING_THESIS_REGISTRATION.toString());
                            message.addReceiver(thesisCommittee[0]);
                            try {
                                message.setContentObject(chosenThesis);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            myAgent.send(message);

                        } else {
                            System.out.println("[ERROR] There are no agents with the service that is being searched for.");
                        }
                    }

                } else if(receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                    System.out.println("[INFO] "+receivedMessage.getSender().getLocalName()+
                            " wants to reject the proposal.");
                    // todo: Send more info about the selected thesis to the student so it can decide to reject or continue with the thesis
                    // todo: if the proposal is rejected then add it back to the proposalList
                }

            }else {
                block();
            }

        }
    }

    private static class ListenAdHocProposals extends CyclicBehaviour {
        private Thesis receivedAdHocThesis;

        public ListenAdHocProposals(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.name()),
                    MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
            ACLMessage receivedMessage = myAgent.receive(messageTemplate);

            if (receivedMessage != null){
                System.out.println("[INFO] Agent:["+myAgent.getLocalName() +"] received an AD-HOC thesis proposal from Agent:["+receivedMessage.getSender().getLocalName()+"]");
                try {
                    receivedAdHocThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] could not read the contents of the message received from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                ACLMessage reply = receivedMessage.createReply();
                if (receivedAdHocThesis.getAcademicWorth() > 50) {
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] accepted AD-HOC THESIS PROPOSAL:["+receivedAdHocThesis.getThesisTitle()+ "] of Agent:["+
                            receivedMessage.getSender().getLocalName()+"]");

                    // Set the supervisor itself as the supervisor of the adhoc thesis
                    receivedAdHocThesis.setThesisSupervisor(myAgent.getAID());

                    // supervisor revises the the proposal
                    receivedAdHocThesis.setRevisedBySupervisor(true);

                    //Put the thesis into the on going thesis list

                    Aulaweb aulaweb = Aulaweb.getInstance();
                    aulaweb.addOnGoingThesesByStudent(receivedMessage.getSender(),receivedAdHocThesis);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] selected itself as the supervisor the Thesis:["+receivedAdHocThesis.getThesisTitle()+"] which will be done by Agent:["+receivedMessage.getSender().getLocalName()+"]");

                    // inform the student that its thesis was accepted
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString());
                    reply.setContent(ProfessorMessageContents.AD_HOC_THESIS_PROPOSAL_ACCEPTED.toString());
                    myAgent.send(reply);

                    // todo: inform thesis committee after registering a thesis as ONGOING
                    AID[] thesisCommittee = Utils.getAgentList(myAgent,"thesis_committee");
                    if (thesisCommittee != null && thesisCommittee.length > 0){
                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                        message.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_ONGOING_THESIS_REGISTRATION.toString());
                        message.addReceiver(thesisCommittee[0]);
                        try {
                            message.setContentObject(receivedAdHocThesis);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        myAgent.send(message);

                    } else {
                        System.out.println("[ERROR] There are no agents with the service that is being searched for.");
                    }

                } else {

                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] informs the Agent:["+
                            receivedMessage.getSender().getLocalName()+"] that the AD-HOC THESIS PROPOSAL:["+ receivedAdHocThesis.getThesisTitle() +"] was REJECTED since it was academically insufficient.");
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString());
                    reply.setContent(ProfessorMessageContents.AD_HOC_THESIS_PROPOSAL_REJECTED.toString());
                    myAgent.send(reply);
                }
            }else {
                block();
            }
        }
    }


    private static class ListenThesisCommitteeForExternalThesis extends CyclicBehaviour{
        private Thesis receivedThesis;

        public ListenThesisCommitteeForExternalThesis(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            // Listen Th committee for external based thesis proposals
            MessageTemplate mtExternalThesis = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.SELECT_SUPERVISOR_FOR_EXTERNAL_THESIS.toString()),
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            ACLMessage receivedMessage = myAgent.receive(mtExternalThesis);

            // check if there is any messages that match our receive template
            if (receivedMessage != null ){
                try {
                    receivedThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent "+myAgent.getLocalName()+" could not read the contents of the message coming from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                if (receivedThesis != null){

                    // supervisor revises the thesis
                    receivedThesis.setRevisedBySupervisor(true);

                    // set this agent as supervisor
                    receivedThesis.setThesisSupervisor(myAgent.getAID());

                    // put the thesis into on going thesis list
                    Aulaweb aulaweb = Aulaweb.getInstance();
                    AID student = receivedThesis.getThesisStudent();

                    // add the thesis into the ON GOING list on Aulaweb
                    assert student != null;
                    aulaweb.addOnGoingThesesByStudent(student, receivedThesis);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] revised the Thesis:["+receivedThesis.getThesisTitle()+" of Agent:"+student.getLocalName()+"], and set it to ON_GOING");
//                    System.out.println(receivedThesis.getThesisStudent().getLocalName() +"   "+ receivedThesis.getThesisSupervisor().getLocalName());

                    // inform thesis committee after registering a thesis as ONGOING
                    AID[] thesisCommittee = Utils.getAgentList(myAgent,"thesis_committee");
                    if (thesisCommittee != null && thesisCommittee.length > 0) {
                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                        message.setConversationId(ConversationIDs.INFORM_THESIS_COMMITTEE_FOR_ONGOING_THESIS_REGISTRATION.toString());
                        message.addReceiver(thesisCommittee[0]);
                        try {
                            message.setContentObject(receivedThesis);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        myAgent.send(message);
                    }else {
                        System.out.println("[ERROR] There are no agents with the service that is being searched for.");
                    }
                }else {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] says: Received thesis is NULL.");
                }


            }else {
                block();
            }

        }
    }
}

