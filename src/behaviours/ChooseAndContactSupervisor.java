package behaviours;

import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;

public class ChooseAndContactSupervisor extends OneShotBehaviour {
    private String researchInterest;
    private Thesis adhocThesis;
    public ChooseAndContactSupervisor(Agent agent, String researchInterest, Thesis adhocThesis) {
        super(agent);
        this.researchInterest = researchInterest;
        this.adhocThesis = adhocThesis;
    }

//    @Override
//    public void reset() {
//        super.reset();
//    }

    @Override
    public void onStart() {
        long restartCount = getRestartCounter();
        System.out.println("\n\n\n\n\n\n");
        System.out.println("########################################################### RESTART COUNT == == == = "+restartCount);
        System.out.println("\n\n\n\n\n\n");
    }

    @Override
    public void action() {
        // contact the supervisors that have the same research interest with the agent
        AID[] supervisorsToContact = Utils.getAgentList(myAgent, researchInterest);
        if(supervisorsToContact != null && supervisorsToContact.length>0){
            ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
            try {
                message.setContentObject(adhocThesis);
            } catch (IOException e) {
                System.out.println("\n[ERROR] Agent "+ myAgent.getLocalName() +" Failed to serialize object.");
                e.printStackTrace();
            }
            message.addReceiver(supervisorsToContact[0]);
            message.setConversationId(ConversationIDs.PROPOSE_ADHOC_THESIS_TO_SUPERVISOR.toString());
            myAgent.send(message);
            System.out.println("[INFO] Agent: "+myAgent.getLocalName()+" sent its AD HOC thesis proposal to Agent: "+supervisorsToContact[0].getLocalName());
        }
    }
}
