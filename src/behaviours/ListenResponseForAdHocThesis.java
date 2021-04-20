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
                System.out.println("[INFO] AD-HOC PROPOSAL of Agent:"+myAgent.getLocalName()+" accepted by Agent:["+receivedMessage.getSender().getLocalName()+"]");

            } else if (receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                System.out.println("[INFO] AD-HOC PROPOSAL of Agent:"+myAgent.getLocalName()+" rejected by Agent:["+receivedMessage.getSender().getLocalName()+"], because the proposal has been found academically insufficient");

                // Randomly choose between;
                // - increasing the academical sufficiency of the AD_HOC thesis
                // - changing students mind to do a PROPOSED thesis
                // - changing students mind to do a EXTERNAL thesis

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
