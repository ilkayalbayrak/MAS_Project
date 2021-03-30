package agents;

import behaviours.OfferCompanyThesisProposals;
import behaviours.OfferThesisProposals;
import jade.core.Agent;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ResearchCenterOrCompany extends Agent {
    private Map<String,String> companyThesisList = new HashMap<>();
    @Override
    protected void setup() {

        System.out.println("Hello ResearchCenterOrCompany " + getAID().getName() + " is ready.");
        //manually set the thesis
        companyThesisList.put("CompanyThesis1", "NLP");
        companyThesisList.put("CompanyThesis2", "NLP");
        companyThesisList.put("CompanyThesis3", "MAS");
        companyThesisList.put("CompanyThesis4", "ML");
        companyThesisList.put("CompanyThesis5", "SPR");

        // Register agent to yellow pages
        String[] serviceTypes = {"research_center", "company"};
        String[] serviceNames = {"research_center", "company"};
        Utils.registerService(this, serviceTypes, serviceNames);

        addBehaviour(new OfferCompanyThesisProposals(this, companyThesisList));
    }

    @Override
    protected void takeDown() {
        Utils.deregister(this);
        System.out.println(this.getLocalName() + " says: I have served my purpose. Now, time has come to set sail for the Undying Lands.");
    }
}
