package utils;

import jade.core.AID;

import java.io.Serializable;


/*
* Thesis class for defining a thesis
* */
public class Thesis implements Serializable {

    // Thesis type: EXTERNAL, PROPOSED, AD_HOC
    private String thesisType;

    // title: NLP_Thesis1
    private String thesisTitle;

    // subject: NATURAL_LANGUAGE_PROCESSING
    private String thesisSubject;


    private String thesisInfo;

    //set a value between 30-100 that indicates academic worth of the thesis
    // there is no control if it's in fact between 30-100 for now
    private int academicWorth;

    // AID of student who works on the thesis: AID of Student1 agent for ex...
    private AID thesisStudent;

    // AID of the supervisor agent
    private AID thesisSupervisor;

    // AID of the reviewer agent
    private AID thesisReviewer;

    // Some boolean checks that are for simulating different phases thesis go through
    private boolean revisedBySupervisor = false;
    private boolean revisedByReviewer = false;
    private boolean discussedWithReviewer = false;



    /////////////////////////////////////////
    ////////// GETTER AND SETTERS ///////////
    /////////////////////////////////////////

    public boolean isDiscussedWithReviewer() {
        return discussedWithReviewer;
    }

    public void setDiscussedWithReviewer(boolean discussedWithReviewer) {
        this.discussedWithReviewer = discussedWithReviewer;
    }

    public boolean isRevisedByReviewer() {
        return revisedByReviewer;
    }

    public void setRevisedByReviewer(boolean revisedByReviewer) {
        this.revisedByReviewer = revisedByReviewer;
    }

    public AID getThesisReviewer() {
        return thesisReviewer;
    }

    public void setThesisReviewer(AID thesisReviewer) {
        this.thesisReviewer = thesisReviewer;
    }

    public boolean isRevisedBySupervisor() {
        return revisedBySupervisor;
    }

    public void setRevisedBySupervisor(boolean revisedBySupervisor) {
        this.revisedBySupervisor = revisedBySupervisor;
    }

    public String getThesisType() {
        return thesisType;
    }

    public void setThesisType(String thesisType) {
        this.thesisType = thesisType;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public String getThesisSubject() {
        return thesisSubject;
    }

    public void setThesisSubject(String thesisSubject) {
        this.thesisSubject = thesisSubject;
    }

    public String getThesisInfo() {
        return thesisInfo;
    }

    public void setThesisInfo(String thesisInfo) {
        this.thesisInfo = thesisInfo;
    }

    public int getAcademicWorth() {
        return academicWorth;
    }

    public void setAcademicWorth(int academicWorth) {
        this.academicWorth = academicWorth;
    }

    public AID getThesisStudent() {
        return thesisStudent;
    }

    public void setThesisStudent(AID thesisStudent) {
        this.thesisStudent = thesisStudent;
    }

    public AID getThesisSupervisor() {
        return thesisSupervisor;
    }

    public void setThesisSupervisor(AID thesisSupervisor) {
        this.thesisSupervisor = thesisSupervisor;
    }
}
