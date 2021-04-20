package agents;

import jade.core.Agent;
import utils.Utils;

public abstract class Reviewer extends Agent {
    String[] serviceTypes;
    String[] serviceNames;

    @Override
    protected void setup() {
        System.out.println("Hello "+getAID().getLocalName()+ " " + getAID().getName() + " is ready.");

        init();
        Utils.registerService(this, serviceTypes, serviceNames);

        // todo: add behaviours

    }

    protected abstract void init();

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
