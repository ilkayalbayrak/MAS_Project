package behaviours;

import agents.Student;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisTypes;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jdk.jshell.execution.Util;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.util.Random;

/*
* Student agent behaviour that listens supervisor to understand if the ad-hoc thesis proposal
* of the student was rejected or accepted
* If accepted student agent follows the rest of the ad-hoc thesis path shown on the Aulaweb(the real one) schema
* If rejected student randomly picks 1 of the 3 options that may make the agent decide to follow
* different thesis paths or, increase the academic value of the current ad-hoc thesis and re-discuss with the supervisor
* */
public class ListenResponseForAdHocThesis extends CyclicBehaviour {
    private Thesis adHocThesis;
    private String researchInterest = null;
    public ListenResponseForAdHocThesis(Agent agent, Thesis adHocThesis) {
        super(agent);
        this.adHocThesis = adHocThesis;
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString());

        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){
            if (receivedMessage.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received a message from Agent:["+receivedMessage.getSender().getLocalName()+"] with the information AD-HOC PROPOSAL ACCEPTED ");

            } else if (receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] received a message from Agent:["+receivedMessage.getSender().getLocalName()+"] with the information AD-HOC PROPOSAL REJECTED because the proposal was academically insufficient");

                // Randomly choose between;
                // - increasing the academical sufficiency of the AD_HOC thesis
                // - changing student's mind to do a PROPOSED thesis
                // - changing student's mind to do a EXTERNAL thesis

                // Generate random number to decide
                // 0: fix AD_HOC thesis
                // 1: Choose PROPOSED
                // 2: Choose EXTERNAL

                Random rand = new Random();
                int max = 2;
                int min = 0;
                int randomNum = rand.nextInt((max - min) +1) +min;

                String thesisType;
                switch (randomNum){
                    case 0:
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] decided to introduce some fixes to increase the academical sufficiency of its AD-HOC thesis proposal");

                        int incAcademicWorth = adHocThesis.getAcademicWorth() + 24;
                        adHocThesis.setAcademicWorth(incAcademicWorth);
                        ACLMessage reply = receivedMessage.createReply();
                        reply.setPerformative(ACLMessage.PROPOSE);
                        try {
                            reply.setContentObject(adHocThesis);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("\n[ERROR] Agent "+ myAgent.getLocalName() +" Failed to serialize object.");
                        }
                        myAgent.send(reply);
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] sent back its AD-HOC proposal to Agent:["+receivedMessage.getSender().getLocalName()+"] for re-evaluation after making some changes");
                        break;

                    case 1:
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] decided to choose a PROPOSED thesis proposal since its AD-HOC thesis idea was not good enough");

                        thesisType = ThesisTypes.PROPOSED.toString();
                        Student.executeChosenThesisPath(myAgent, thesisType, researchInterest, null);

                        break;
                    case 2:
                        System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] decided to choose an EXTERNAL thesis proposal since its AD-HOC thesis idea was not good enough");

                        thesisType = ThesisTypes.EXTERNAL.toString();
                        Student.executeChosenThesisPath(myAgent, thesisType, researchInterest, null);
                        break;
                }
            }
        }else {
            block();
        }
    }
}
