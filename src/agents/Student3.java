package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class Student3 extends Student {

    @Override
    protected void init() {
        Thesis thesisIdea = new Thesis();
        thesisIdea.setThesisSupervisor(null);
        thesisIdea.setThesisType(ThesisTypes.AD_HOC.toString());
        thesisIdea.setThesisTitle("AD_HOC_Thesis_1");
        thesisIdea.setThesisSubject(researchInterest);
        thesisIdea.setThesisInfo("Some random thesis INFO");
        thesisIdea.setAcademicWorth(31);
        thesisIdea.setThesisStudent(this.getAID());

        serviceNames = new String[]{"student3"};
        adHocThesis = thesisIdea;
    }
}
