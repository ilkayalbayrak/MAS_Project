package agents;

import jade.core.Agent;

public class Reviewer1 extends Agent {
    protected void setup(){
        System.out.println("Hello Reviewer1 " + getAID().getName() + " is ready.");
    }
    protected void takeDown(){
        System.out.println("Reviewer1 " +  getAID().getName() + " says:I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
