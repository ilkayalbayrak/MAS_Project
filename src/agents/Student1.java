package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

public class Student1 extends Student {

    @Override
    protected void init() {
        Thesis thesisIdea = new Thesis();
        thesisIdea.setThesisSupervisor(null);
        thesisIdea.setThesisType(ThesisTypes.AD_HOC.toString());
        thesisIdea.setThesisTitle("AD_HOC_Thesis_3");
        thesisIdea.setThesisSubject(researchInterest);
        thesisIdea.setThesisInfo("Some random thesis INFO about some interesting topic that could be a thesis topic");
        thesisIdea.setAcademicWorth(37);
        thesisIdea.setThesisStudent(this.getAID());

        serviceNames=new String[]{"student1"};
        adHocThesis = thesisIdea;
    }
}
