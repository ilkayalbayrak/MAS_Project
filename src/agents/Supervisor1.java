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

import java.util.*;


public class Supervisor1 extends Agent {
    private List<Thesis> proposalList = new LinkedList<>();
    private Map<AID, Thesis> onGoingThesesList = new HashMap<>();

    public Map<AID, Thesis> getOnGoingThesesList() {
        return onGoingThesesList;
    }

    public void setOnGoingTheses(Thesis thesis, AID student) {
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
        thesis1.setThesisTitle("NLP_Thesis1");
        thesis1.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis1.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis1.setAcademicWorth(90);

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis2.setThesisTitle("NLP_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis2.setAcademicWorth(100);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis3.setThesisTitle("NLP_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis3.setAcademicWorth(75);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis4.setThesisTitle("NLP_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis4.setAcademicWorth(87);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis5.setThesisTitle("NLP_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROPOSAL.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of NLP");
        thesis5.setAcademicWorth(80);

        proposalList.add(thesis1);
        proposalList.add(thesis2);
        proposalList.add(thesis3);
        proposalList.add(thesis4);
        proposalList.add(thesis5);
    }


    protected void setup(){
        System.out.println("Hello Supervisor1 " + getAID().getName() + " is ready.");
        // Available thesis proposals of the supervisor
        initThesisProposals();

        // Register agent to yellow pages
        String[] serviceTypes = {"supervisor", "NATURAL_LANGUAGE_PROCESSING"};
        String[] serviceNames = {"professor","professor_NLP"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferThesisProposals(this, proposalList));
        addBehaviour(new HandleThesisAcceptances());
        addBehaviour(new ListenAdHocProposals(this));
//        addBehaviour(new ListenStudents(this));
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

                    Thesis chosenThesisTitle = null;
                    try {
                        chosenThesisTitle = (Thesis) receivedMessage.getContentObject();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                    removeProposal(chosenThesisTitle);
                    System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesisTitle+
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
                    setOnGoingTheses(receivedAdHocThesis, receivedMessage.getSender());
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

}

