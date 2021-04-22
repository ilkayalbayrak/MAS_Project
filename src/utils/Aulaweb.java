package utils;

import jade.core.AID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
* Aulaweb is a single instance class works as a global platform for storing the
* On Going theses
* */
public final class Aulaweb implements Serializable {
    private static Aulaweb INSTANCE = null;
    private String info = "Singleton class for globally shared variable";
    private Map<AID, Thesis> onGoingThesesByStudent = new HashMap<>();

    private Aulaweb() {
    }

    public synchronized static Aulaweb getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Aulaweb();
        }
        return INSTANCE;
    }

    /////////////////////////////////////////
    ////////// GETTER AND SETTERS ///////////
    /////////////////////////////////////////

    public String getInfo() {
        return info;
    }

    public Map<AID, Thesis> getOnGoingThesesByStudent() {
        return onGoingThesesByStudent;
    }

    public void removeOnGoingTheseByStudent(Thesis thesis){
        this.onGoingThesesByStudent.remove(thesis);
    }

    public void updateOnGoingThesesByStudent(AID student, Thesis thesis){
        if(this.getOnGoingThesesByStudent().containsKey(student)){
            this.onGoingThesesByStudent.put(student,thesis);
        }else{
            System.out.println("[ERROR] The Agent:["+student.getLocalName()+"] does not have a registered thesis on Aulaweb, therefore can not use the (updateOnGoingThesesByStudent) function");
        }
    }

    public void addOnGoingThesesByStudent(AID student, Thesis thesis) {
        this.onGoingThesesByStudent.put(student, thesis);
    }
}
