package agents;

import interfaces.enums.ServiceTypes;
import jade.core.Agent;
import utils.Utils;

public class Reviewer5 extends Reviewer {

    @Override
    protected void init() {
        serviceTypes = new String[]{ServiceTypes.REVIEWER.toString(), "REVIEWER_SPEECH_PROCESSING_AND_RECOGNITION"};
        serviceNames = new String[]{"professor","professor_SPR"};
    }
}
