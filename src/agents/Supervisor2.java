package agents;

import behaviours.OfferThesisProposals;
import interfaces.SupervisorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisMainSubjects;
import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

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

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis2.setThesisTitle("ML_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis2.setAcademicWorth(97);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis3.setThesisTitle("ML_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis3.setAcademicWorth(67);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis4.setThesisTitle("ML_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis4.setAcademicWorth(72);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis5.setThesisTitle("ML_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of ML");
        thesis5.setAcademicWorth(81);

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
        addBehaviour(new ListenAdHocProposals(this));
        addBehaviour(new ListenThesisCommittee(this));
//

    }
    protected void takeDown(){
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

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
                        e.printStackTrace();
                    }
                    removeProposal(chosenThesis);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesis.getThesisTitle()+
                            " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");
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
                System.out.println("[INFO] Agent "+myAgent.getLocalName() +" received message from "+receivedMessage.getSender().getName());
                try {
                    receivedAdHocThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent: "+myAgent.getLocalName()+" could not read the contents of the message received from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                ACLMessage reply = receivedMessage.createReply();
                if (receivedAdHocThesis.getAcademicWorth() > 50) {
                    receivedAdHocThesis.setThesisSupervisor(myAgent.getAID());
                    setOnGoingTheses(receivedMessage.getSender(), receivedAdHocThesis);
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(SupervisorMessageContents.AD_HOC_THESIS_PROPOSAL_ACCEPTED);
                    myAgent.send(reply);
                    System.out.println("[INFO] Agent: "+myAgent.getLocalName()+" accepted AD-HOC thesis proposal of Agent: "+
                            receivedMessage.getSender().getLocalName());
                } else {

                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString()+receivedMessage.getSender().getLocalName());
                    reply.setContent(SupervisorMessageContents.AD_HOC_THESIS_PROPOSAL_REJECTED);
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
                System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" ################## MESSAGG E IS NOT NUUUUUULLLLLLLLL");
                //Get the external TH proposal from the msg content
//                Thesis receivedThesis = null;
                try {
                    receivedThesis = (Thesis) receivedMessage.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("[ERROR] Agent "+myAgent.getLocalName()+" could not read the contents of the message coming from Agent: "+receivedMessage.getSender().getLocalName());
                    e.printStackTrace();
                }
                if (receivedThesis != null){
                    System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" ################## THESISISIISISISIISIS IS NOT NUUUUUULLLLLLLLL");
                    // Revise the received thesis proposal before placing it into the "ongoing thesis" bucket
                    receivedThesis.setRevisedBySupervisor(true);

                    // put the thesis into on going thesis list
                    AID student = receivedThesis.getThesisStudent();
                    setOnGoingTheses(student, receivedThesis);

                    System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" revised the Thesis:"+receivedThesis.getThesisTitle()+" of Agent:"+student.getLocalName()+", and set it to ON_GOING");

                }else {
                    System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" ################## THESISISIISISISIISIS IS NUUUUUULLLLLLLLL sssssssssssssssssssoooooooooooorrrrrrrrrrrrrrrrrryyyyyyyyyyy");
                }


                }else {
                block();
            }
        }
    }
}
