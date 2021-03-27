package agents;

import jade.core.Agent;

public class Supervisor2 extends Agent {
    protected void setup(){
        System.out.println("Hello Supervisor2 " + getAID().getName() + " is ready.");
    }
    protected void takeDown(){
        System.out.println("Supervisor2 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
