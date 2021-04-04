package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class Student3 extends Agent {
    private AID[] supervisors;
    private String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed
    private String researchInterest;

    @Override
    protected void setup() {
        System.out.println("Hello Student3 " + getAID().getName() + " is ready.");

        // the custom thesis that student wants to do
        Thesis adhocThesis = new Thesis();
        adhocThesis.setThesisType(ThesisTypes.AD_HOC.toString());
        adhocThesis.setThesisTitle("AD_HOC_Thesis_1");
        adhocThesis.setThesisSubject(researchInterest);
        adhocThesis.setThesisInfo("Some random thesis INFO");
        adhocThesis.setAcademicWorth(67);
        adhocThesis.setThesisStudent(this.getAID());

        Object[] args = getArguments();
        if (args != null && args.length >0){
            thesisType = (String) args[0];
            researchInterest = (String) args[1];
        }

        thesisType = Utils.getThesisTypeArgument(this);

        // Register agent to yellow pages
        String[] serviceNames = {"student3"};
        String[] serviceTypes = {"student"};
        Utils.registerService(this, serviceTypes, serviceNames);

        Utils.executeChosenThesisPath(this, thesisType, researchInterest, adhocThesis);
    }

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
