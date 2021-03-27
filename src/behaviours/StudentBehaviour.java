package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class StudentBehaviour extends OneShotBehaviour {
    public StudentBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("agents.Supervisor1", AID.ISLOCALNAME));
        msg.setContent("Student asking for th proposals");
        System.out.println(msg);
        myAgent.send(msg);
    }
}
