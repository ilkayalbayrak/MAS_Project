package agents;

import behaviours.ListenInitialProposalRejections;
import behaviours.OfferThesisProposals;
import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ThesisMainSubjects;
import interfaces.enums.ThesisTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Aulaweb;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Supervisor5 extends Supervisor {

    @Override
    protected void initThesisProposals() {
        // Available thesis proposals of the supervisor
        Thesis thesis1 = new Thesis();
        thesis1.setThesisSupervisor(this.getAID());
        thesis1.setThesisStudent(null);
        thesis1.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis1.setThesisTitle("SPR_Thesis1");
        thesis1.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis1.setThesisInfo("Some imaginary info about a research topic within the borders of SPEECH PROCESSING");
        thesis1.setAcademicWorth(90);
        thesis1.setRevisedBySupervisor(true);

        Thesis thesis2 = new Thesis();
        thesis2.setThesisSupervisor(this.getAID());
        thesis2.setThesisStudent(null);
        thesis2.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis2.setThesisTitle("SPR_Thesis2");
        thesis2.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis2.setThesisInfo("Some imaginary info about a research topic within the borders of SPEECH PROCESSING");
        thesis2.setAcademicWorth(100);
        thesis2.setRevisedBySupervisor(true);

        Thesis thesis3 = new Thesis();
        thesis3.setThesisSupervisor(this.getAID());
        thesis3.setThesisStudent(null);
        thesis3.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis3.setThesisTitle("SPR_Thesis3");
        thesis3.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis3.setThesisInfo("Some imaginary info about a research topic within the borders of SPEECH PROCESSING");
        thesis3.setAcademicWorth(75);
        thesis3.setRevisedBySupervisor(true);

        Thesis thesis4 = new Thesis();
        thesis4.setThesisSupervisor(this.getAID());
        thesis4.setThesisStudent(null);
        thesis4.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis4.setThesisTitle("SPR_Thesis4");
        thesis4.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis4.setThesisInfo("Some imaginary info about a research topic within the borders of SPEECH PROCESSING");
        thesis4.setAcademicWorth(87);
        thesis4.setRevisedBySupervisor(true);

        Thesis thesis5 = new Thesis();
        thesis5.setThesisSupervisor(this.getAID());
        thesis5.setThesisStudent(null);
        thesis5.setThesisType(ThesisTypes.PROPOSED.toString());
        thesis5.setThesisTitle("SPR_Thesis5");
        thesis5.setThesisSubject(ThesisMainSubjects.SPEECH_PROCESSING_AND_RECOGNITION.toString());
        thesis5.setThesisInfo("Some imaginary info about a research topic within the borders of SPEECH PROCESSING");
        thesis5.setAcademicWorth(80);
        thesis5.setRevisedBySupervisor(true);

        proposalList.add(thesis1);
        proposalList.add(thesis2);
        proposalList.add(thesis3);
        proposalList.add(thesis4);
        proposalList.add(thesis5);

    }

    @Override
    protected void init() {
        String[] serviceTypes = {"supervisor", "SPEECH_PROCESSING_AND_RECOGNITION"};
        String[] serviceNames = {"professor", "professor_SPR"};
    }
}