package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class Student5 extends Student {
    @Override
    protected void init() {
        Thesis thesisIdea = new Thesis();
        thesisIdea.setThesisSupervisor(null);
        thesisIdea.setThesisType(ThesisTypes.AD_HOC.toString());
        thesisIdea.setThesisTitle("AD_HOC_Thesis_5");
        thesisIdea.setThesisSubject(researchInterest);
        thesisIdea.setThesisInfo("Some random thesis INFO about some interesting topic that could be a thesis topic");
        thesisIdea.setAcademicWorth(71);
        thesisIdea.setThesisStudent(this.getAID());

        serviceNames = new String[]{"student5"};
        adHocThesis = thesisIdea;
    }
//    private AID[] supervisors;
//    private String thesisType; // which kind of thesis the student will opt for: external, ad-hoc, proposed
//    private String researchInterest;
//    @Override
//    protected void setup() {
//        System.out.println("Hello Student5 " + getAID().getName() + " is ready.");
//
//        Object[] args = getArguments();
//        if (args != null && args.length >0){
//            thesisType = (String) args[0];
//            researchInterest = (String) args[1];
//        }
//
//        // Register agent to yellow pages
//        String[] serviceNames = {"student5"};
//        String[] serviceTypes = {"student"};
//        Utils.registerService(this, serviceTypes, serviceNames);
//
//        Utils.executeChosenThesisPath(this, thesisType, researchInterest,null);
//    }
//
//    @Override
//    protected void takeDown() {
//        Utils.deregister(this);
//        System.out.println(this.getAID().getName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
//    }

}
