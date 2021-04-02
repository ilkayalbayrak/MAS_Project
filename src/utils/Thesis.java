package utils;

import jade.core.AID;

import java.io.Serializable;

public class Thesis implements Serializable {

    // thsis type: external, proposed etc.
    private String thesisType;
    private String thesisTitle;
    private String thesisSubject;
    private String thesisInfo;
    private int academicWorth;
    private AID thesisStudent;
    private AID thesisSupervisor;

    public Thesis(String thesisType, String thesisTitle, String thesisSubject, String thesisInfo, int academicWorth, AID thesisStudent, AID thesisSupervisor) {
        this.thesisType = thesisType;
        this.thesisTitle = thesisTitle;
        this.thesisSubject = thesisSubject;
        this.thesisInfo = thesisInfo;
        this.academicWorth = academicWorth;
        this.thesisStudent = thesisStudent;
        this.thesisSupervisor = thesisSupervisor;
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
