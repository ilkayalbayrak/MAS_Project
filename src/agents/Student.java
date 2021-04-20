package agents;

import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

import java.util.Collections;

public abstract class Student extends Agent {
    AID[] supervisors;
    String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed
    String researchInterest;
    final String[] serviceTypes= {"student"};
    String[] serviceNames;
    Thesis adHocThesis;

    @Override
    protected void setup() {
        System.out.println("Hello "+getAID().getLocalName()+ " " + getAID().getName() + " is ready.");

        Object[] args = getArguments();
        if (args != null && args.length >0){
            thesisType = (String) args[0];
            researchInterest = (String) args[1];
        }

        init();
        // Register agent to yellow pages
        Utils.registerService(this, serviceTypes, serviceNames);

        Utils.executeChosenThesisPath(this, thesisType, researchInterest, adHocThesis);
    }

    protected abstract void init();

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }

}
