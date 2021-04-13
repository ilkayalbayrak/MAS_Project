package agents;

import behaviours.ListenInitialProposalRejections;
import behaviours.OfferThesisProposals;
import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisMainSubjects;
import interfaces.enums.ThesisTypes;
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

public class Supervisor2 extends Agent {
    private List<Thesis> proposalList = new LinkedList<>();
    private Map<AID, Thesis> onGoingThesesList = new HashMap<>();

    public Map<AID, Thesis> getOnGoingThesesList() {
        return onGoingThesesList;
    }

    public void setOnGoingTheses(AID student, Thesis thesis) {
        onGoingThesesList.put(student, thesis);
    }

    public void setProposalList(Thesis thesis){
        this.proposalList.add(thesis);
    }

    public List<Thesis> getProposalList(){
        return this.proposalList;
    }

    public void removeProposal(Thesis chosenThesis){
        this.proposalList.remove(chosenThesis);
    }

    public void initThesisProposals(){
        // Available thesis proposals of the supervisor
        Thesis thesis1 = new Thesis();
        thesis1.setThesisSupervisor(this.getAID());
        thesis1.setThesisStudent(null);
        thesis1.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis1.setThesisTitle("ML_Thesis1");
        thesis1.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis1.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis1.setAcademicWorth(55);
        thesis1.setRevisedBySupervisor(true);

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis2.setThesisTitle("ML_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis2.setAcademicWorth(97);
        thesis2.setRevisedBySupervisor(true);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis3.setThesisTitle("ML_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis3.setAcademicWorth(67);
        thesis3.setRevisedBySupervisor(true);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis4.setThesisTitle("ML_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis4.setAcademicWorth(72);
        thesis4.setRevisedBySupervisor(true);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis5.setThesisTitle("ML_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis5.setAcademicWorth(81);
        thesis5.setRevisedBySupervisor(true);

        proposalList.add(thesis1);
        proposalList.add(thesis2);
        proposalList.add(thesis3);
        proposalList.add(thesis4);
        proposalList.add(thesis5);
    }

    protected void setup(){
        System.out.println("Hello Supervisor2 " + getAID().getName() + " is ready.");

        initThesisProposals();

        // Register agent to yellow pages
        String[] serviceTypes = {"supervisor", "MACHINE_LEARNING"};
        String[] serviceNames = {"professor","professor_ML"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
        addBehaviour(new ListenInitialProposalRejections(this));
        addBehaviour(new ListenAdHocProposals(this));
        addBehaviour(new ListenThesisCommittee(this));

    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

    // For the PROPOSED THESIS PATH
    // Receive info about which one of supervisor's TH proposals have been chosen by a student agent
    // Remove the chosen proposal from the supervisor's proposal list
    // Put the chosen thesis and student agent's name into the ON_GOING thesis list
    private class HandleThesisAcceptances extends CyclicBehaviour {

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
                        System.out.println("[ERROR] Agent "+ myAgent.getLocalName()+ " could not extract the proposals object from message");
                        e.printStackTrace();
                    }

                    assert chosenThesis != null;

                    // get instance of Aulaweb class to get ONGOING THESES
                    Aulaweb aulaweb = Aulaweb.getInstance();

                    // todo: Check if the chosen thesis is already in the ON GOING theses list

                    // if the chosen title already been picked, send the AVAILABLE proposals so the agent can pick another thesis proposal
                    if (aulaweb.getONGOING_THESES().containsValue(chosenThesis)){
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] says: Thesis:["+chosenThesis.getThesisTitle()+"] has already been chosen by another agent and informs the Agent:["+receivedMessage.getSender().getLocalName()+"] about the situation.");

                        ACLMessage reply = receivedMessage.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setConversationId(ConversationIDs.THESIS_ALREADY_BEEN_PICKED.toString());

                        try {
                            reply.setContentObject((Serializable) getProposalList());
                        } catch (IOException e) {
                            System.out.println("\n[ERROR] Agent:["+ myAgent.getLocalName() +"] Failed to serialize its proposalList object.");
                            e.printStackTrace();
                        }

                        myAgent.send(reply);

                    } else{
                        // if the chosen thesis has not been already picked by another student agent
                        // add the thesis on the Aulaweb ONGOING THESES list
                        // Then remove the chosen thesis from the personal proposal offerings of the supervisor agent
                        removeProposal(chosenThesis);
                        System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesis.getThesisTitle()+
                                " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");

                        AID student = chosenThesis.getThesisStudent();
                        aulaweb.addONGOING_THESES(student,chosenThesis);
                        System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" revised the Thesis:"+chosenThesis.getThesisTitle()+" of Agent:"+student.getLocalName()+", and set it to ON_GOING");

                        // todo: inform thesis committe after registering a thesis as ONGOING
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
                    System.out.println("\n[INFO] "+receivedMessage.getSender().getLocalName()+
                            " wants to reject the proposal.");
                    // todo: Send more info about the selected thesis to the student so it can decide to reject or continue with the thesis
                    // todo: if the proposal is rejected then add it back to the proposalList
                }

            }else {
                block();
            }

        }
    }

    private class ListenAdHocProposals extends CyclicBehaviour {
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
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] accepted AD-HOC thesis proposal of Agent: "+
                            receivedMessage.getSender().getLocalName());

                    // Set the supervisor itself as the supervisor of the adhoc thesis
                    receivedAdHocThesis.setThesisSupervisor(myAgent.getAID());

                    // supervisor revises the the proposal
                    receivedAdHocThesis.setRevisedBySupervisor(true);

                    //Put the thesis into the on going thesis list

                    Aulaweb aulaweb = Aulaweb.getInstance();
                    aulaweb.addONGOING_THESES(receivedMessage.getSender(),receivedAdHocThesis);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] selected itself as the supervisor the Thesis:["+receivedAdHocThesis.getThesisTitle()+"] which will be done by Agent:["+receivedMessage.getSender().getLocalName()+"]");
//                    System.out.println("\n\n\n\n\n"+aulaweb.getONGOING_THESES()+"\n\n\n\n\n");

                    // inform the student that its thesis was accepted
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(ProfessorMessageContents.AD_HOC_THESIS_PROPOSAL_ACCEPTED);
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

                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(ProfessorMessageContents.AD_HOC_THESIS_PROPOSAL_REJECTED);
                    myAgent.send(reply);
                    System.out.println("[INFO] Agent: "+myAgent.getLocalName()+" rejected AD-HOC thesis proposal of Agent: "+
                            receivedMessage.getSender().getLocalName());
                }
            }else {
                block();
            }
        }
    }


    private class ListenThesisCommittee extends CyclicBehaviour{
        private Thesis receivedThesis;

        public ListenThesisCommittee(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            // Listen Th committee for external based thesis proposals
            MessageTemplate mtExternalThesis = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.SELECT_SUPERVISOR_FOR_EXTERNAL_THESIS.toString()),
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            ACLMessage receivedMessage = myAgent.receive(mtExternalThesis);

            if (receivedMessage != null ){
//                System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" ################## MESSAGG E IS NOT NUUUUUULLLLLLLLL");
                //Get the external TH proposal from the msg content
//                Thesis receivedThesis = null;
                try {
                    receivedThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent "+myAgent.getLocalName()+" could not read the contents of the message coming from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                if (receivedThesis != null){
//                    System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" ################## THESISISIISISISIISIS IS NOT NUUUUUULLLLLLLLL");
                    // Revise the received thesis proposal before placing it into the "ongoing thesis" bucket
                    receivedThesis.setRevisedBySupervisor(true);

                    // put the thesis into on going thesis list
                    Aulaweb aulaweb = Aulaweb.getInstance();
                    AID student = receivedThesis.getThesisStudent();

                    assert student != null;
                    aulaweb.addONGOING_THESES(student, receivedThesis);
                    System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] revised the Thesis:["+receivedThesis.getThesisTitle()+" of Agent:"+student.getLocalName()+"], and set it to ON_GOING");


                }else {
                    System.out.println("[ERROR] Agent:["+myAgent.getLocalName()+"] says: Received thesis is NULL.");
                }


            }else {
                block();
            }

        }
    }
}
