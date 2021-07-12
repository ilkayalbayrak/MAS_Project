package agents;

import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

import java.util.Collection;
import java.util.Collections;

public class Student2 extends Student{

    @Override
    protected void init() {
        Thesis thesisIdea = new Thesis();
        thesisIdea.setThesisSupervisor(null);
        thesisIdea.setThesisType(ThesisTypes.AD_HOC.toString());
        thesisIdea.setThesisTitle("AD_HOC_Thesis_4");
        thesisIdea.setThesisSubject(researchInterest);
        thesisIdea.setThesisInfo("Some random thesis INFO about some interesting topic that could be a thesis topic");
        thesisIdea.setAcademicWorth(45);
        thesisIdea.setThesisStudent(this.getAID());

        serviceNames = new String[]{"student2"};
        adHocThesis = thesisIdea;
    }
}
