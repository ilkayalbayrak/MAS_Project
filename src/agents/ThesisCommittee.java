package agents;

import jade.core.Agent;

public class ThesisCommittee extends Agent {
    protected void setup(){
        System.out.println("Hello Thesis Committee " + getAID().getName() + " is ready.");
    }
    protected void takeDown(){
        System.out.println("Thesis Committee " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
