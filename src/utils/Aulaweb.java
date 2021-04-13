package utils;

import jade.core.AID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class Aulaweb implements Serializable {
    private static Aulaweb INSTANCE = null;
    private String info = "Singleton class for globally shared variable";
    private Map<AID, Thesis> ONGOING_THESES = new HashMap<>();


    public String getInfo() {
        return info;
    }

    public Map<AID, Thesis> getONGOING_THESES() {
        return ONGOING_THESES;
    }

    public void removeONGOING_THESES(Thesis thesis){
        this.ONGOING_THESES.remove(thesis);
    }

    public void updateONGOING_THESES(AID student, Thesis thesis){
        if(this.getONGOING_THESES().containsValue(thesis)){

        }
    }

    public void addONGOING_THESES(AID student, Thesis thesis) {
//        if ()
        this.ONGOING_THESES.put(student, thesis);
    }

    private Aulaweb() {
    }

    public synchronized static Aulaweb getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Aulaweb();
        }
        return INSTANCE;
    }

    // getters and setters
}
