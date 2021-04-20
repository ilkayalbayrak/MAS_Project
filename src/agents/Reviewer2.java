package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer2 extends Reviewer {
    @Override
    protected void init() {
        serviceTypes = new String[]{ServiceTypes.REVIEWER.toString(), "REVIEWER_MACHINE_LEARNING"};
        serviceNames = new String[]{"professor","professor_ML"};
    }
}
