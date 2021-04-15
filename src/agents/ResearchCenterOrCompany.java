package agents;

import behaviours.OfferCompanyThesisProposals;
import behaviours.OfferThesisProposals;
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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResearchCenterOrCompany extends Agent {
    private List<Thesis> companyThesisList = new LinkedList<>();
    private Map<AID, Thesis> onGoingThesesList = new HashMap<>();
    public Map<AID, Thesis> getOnGoingThesesList() {
        return onGoingThesesList;
    }

    public void setOnGoingTheses(AID student, Thesis thesis) {
        onGoingThesesList.put(student, thesis);
    }
    public void setProposalList(Thesis thesis){
        this.companyThesisList.add(thesis);
    }

    public List<Thesis> getProposalList(){
        return this.companyThesisList;
    }

    public void removeProposal(Thesis chosenThesis){

        this.companyThesisList.remove(chosenThesis);
    }

    public void initThesisProposals(){
        // Available thesis proposals of the supervisor
        Thesis thesis1 = new Thesis();
        thesis1.setThesisSupervisor(this.getAID());
        thesis1.setThesisStudent(null);
        thesis1.setThesisType(ThesisTypes.EXTERNAL.toString());
        thesis1.setThesisTitle("External_Thesis1");
        thesis1.setThesisSubject(ThesisMainSubjects.COMPUTER_VISION.toString());
        thesis1.setThesisInfo("Some imaginary info about a research topic within the borders of External");
        thesis1.setAcademicWorth(60);

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.EXTERNAL.toString());
        thesis2.setThesisTitle("External_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.MULTI_AGENT_SYSTEMS.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of External");
        thesis2.setAcademicWorth(47);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.EXTERNAL.toString());
        thesis3.setThesisTitle("External_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of External");
        thesis3.setAcademicWorth(100);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.EXTERNAL.toString());
        thesis4.setThesisTitle("External_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.NATURAL_LANGUAGE_PROCESSING.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of External");
        thesis4.setAcademicWorth(87);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.EXTERNAL.toString());
        thesis5.setThesisTitle("External_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.MACHINE_LEARNING.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of External");
        thesis5.setAcademicWorth(35);

        companyThesisList.add(thesis1);
        companyThesisList.add(thesis2);
        companyThesisList.add(thesis3);
        companyThesisList.add(thesis4);
        companyThesisList.add(thesis5);
    }

    @Override
    protected void setup() {

        System.out.println("Hello ResearchCenterOrCompany " + getAID().getName() + " is ready.");

        initThesisProposals();
        // Register agent to yellow pages
        String[] serviceTypes = {"research_center", "company"};
        String[] serviceNames = {"research_center", "company"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferCompanyThesisProposals(this, companyThesisList));
        addBehaviour(new HandleThesisAcceptances());
    }

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getLocalName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
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
                    // todo: Check if the chosen thesis is already in the ON GOING theses list
                    // if the chosen title already been picked send AVAILABLE proposals so the agent can pick one of them
                    if (getOnGoingThesesList().containsValue(chosenThesis)){
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
                        removeProposal(chosenThesis);
                        System.out.println("[INFO] Agent "+myAgent.getLocalName()+" removed the Thesis topic: "+ chosenThesis.getThesisTitle()+
                                " chosen by Agent: "+ receivedMessage.getSender().getLocalName()+ " from its available thesis proposals list.");

                        AID student = chosenThesis.getThesisStudent();
                        setOnGoingTheses(student,chosenThesis);
                        System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" revised the Thesis:"+chosenThesis.getThesisTitle()+" of Agent:"+student.getLocalName()+", and set it to ON_GOING");
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


}
