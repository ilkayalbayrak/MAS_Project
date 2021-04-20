package agents;

import jade.core.AID;
import jade.core.Agent;
import utils.Thesis;
import utils.Utils;

import java.util.Collection;
import java.util.Collections;

public class Student2 extends Student{

    @Override
    protected void init() {
        serviceNames = new String[]{"student2"};
        adHocThesis = null;
    }
}
