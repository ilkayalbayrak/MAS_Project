package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer3 extends Reviewer {

    @Override
    protected void init() {
        serviceTypes = new String[]{ServiceTypes.REVIEWER.toString(), "REVIEWER_MULTI_AGENT_SYSTEMS"};
        serviceNames = new String[]{"professor","professor_MAS"};
    }
}
