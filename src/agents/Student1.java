package agents;

import jade.core.AID;
import jade.core.Agent;
import utils.Utils;

public class Student1 extends Student {


    @Override
    protected void init() {
        serviceNames=new String[]{"student1"};
        adHocThesis = null;
    }
}
