package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer1 extends Reviewer {

    @Override
    protected void init() {
        serviceTypes = new String[]{ServiceTypes.REVIEWER.toString(), "REVIEWER_NATURAL_LANGUAGE_PROCESSING"};
        serviceNames = new String[]{"professor","professor_NLP"};
    }
}
