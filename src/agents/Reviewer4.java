package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer4 extends Reviewer {

    @Override
    protected void init() {
        serviceTypes = new String[]{ServiceTypes.REVIEWER.toString(), "REVIEWER_COMPUTER_VISION"};
        serviceNames = new String[]{"professor","professor_CV"};
    }
}
