package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class Student4 extends Student {

    @Override
    protected void init() {// the custom thesis that student wants to do
        Thesis thesisIdea = new Thesis();
        thesisIdea.setThesisSupervisor(null);
        thesisIdea.setThesisReviewer(null);
        thesisIdea.setThesisType(ThesisTypes.AD_HOC.toString());
        thesisIdea.setThesisTitle("AD_HOC_Thesis_2");
        thesisIdea.setThesisSubject(researchInterest);
        thesisIdea.setThesisInfo("Some random thesis INFO about a very cool research idea coming from a genius student");
        thesisIdea.setAcademicWorth(100);
        thesisIdea.setThesisStudent(this.getAID());

        serviceNames = new String[]{"student4"};
        adHocThesis = thesisIdea;


    }
}
