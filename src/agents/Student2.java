package agents;

import jade.core.AID;
import jade.core.Agent;
import utils.Utils;

public class Student2 extends Agent {
    private AID[] supervisors;
    private String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed

    @Override
    protected void setup() {
        System.out.println("Hello Student2 " + getAID().getName() + " is ready.");

        thesisType = Utils.getThesisTypeArgument(this);

        // Register agent to yellow pages
        String[] serviceNames = {"student2"};
        String[] serviceTypes = {"student"};
        Utils.registerService(this, serviceTypes, serviceNames);

        Utils.executeChosenThesisPath(this, thesisType);
    }

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

}
