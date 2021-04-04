package utils;

import behaviours.ChooseCompanyThesisProposal;
import behaviours.ContactSupervisor;
import behaviours.RequestExternalThesisProposals;
import behaviours.RequestThesisProposals;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Utils {

    // Method for registering the agent services to yellow pages
    public static void registerService(Agent agent, String[] serviceTypes, String[] serviceNames){
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(agent.getAID());
        ServiceDescription serviceDescription = null;
        for (int i = 0; i < serviceTypes.length; i++){
            serviceDescription = new ServiceDescription();
            serviceDescription.setType(serviceTypes[i]);
            serviceDescription.setName(serviceNames[i]);
            agentDescription.addServices(serviceDescription);
        }

        try{
            DFService.register(agent, agentDescription);
        } catch (FIPAException e) {
            System.out.println("[ERROR] Agent registration failed.");
            e.printStackTrace();
        }
    }

    public static void deregister(Agent agent) {
        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            System.out.println("[ERROR] Agent de-registration failed.");
            e.printStackTrace();
        }
    }

    // Method for searching agents that offer specified services
    public static DFAgentDescription[] searchServices(Agent agent, String serviceType){
        DFAgentDescription searchTemplate = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(serviceType);
        searchTemplate.addServices(serviceDescription);

        try{
            return DFService.search(agent, searchTemplate);
        } catch (FIPAException e) {
            System.out.println("[ERROR] Failed to search for agent services.");
            e.printStackTrace();
        }
        return null;
    }
    // agent is myagent, agents is the agents we are searching for, and service is our search context for the agents
    public static AID[] getAgentList(Agent agent, String service){
        DFAgentDescription[] descriptions = searchServices(agent, service);
        if(descriptions != null){
            AID[] offeringAgents = new AID[descriptions.length];
            for(int i = 0; i< offeringAgents.length; i++){
                offeringAgents[i] = descriptions[i].getName();
            }
            return offeringAgents;
        } else {
            System.out.println("[INFO] There are no agents in yellow pages that offer the service: {"+service+"}.");
            return null;
        }
    }

    public static String pickRandomThesis(Map<String,String> list){
        if(list != null){
            Object randomThesis = list.keySet().toArray()[new Random().nextInt(list.keySet().toArray().length)];
            return randomThesis.toString();
        }
        System.out.println("\n[ERROR] pickRandomThesis list is empty...");
        return null;

    }

    public static String getThesisTypeArgument(Agent agent){
        String thesisType = null;
        Object[] args = agent.getArguments();
        if (args != null && args.length >0){
            thesisType = (String) args[0];
        }
        return thesisType;
    }

    public static void executeChosenThesisPath(Agent agent, String thesisType, String researchInterest, Thesis adhocThesis){
        if (agent != null && thesisType != null){
            switch (thesisType){
                case "EXTERNAL":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the EXTERNAL TH path ");
                    // then ask Thesis committee if that is acceptable
                    agent.addBehaviour(new RequestExternalThesisProposals(agent));
                    agent.addBehaviour(new ChooseCompanyThesisProposal(agent));
                    break;
                case "AD_HOC":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the AD_HOC TH path ");
                    //choose supervisor and contact supervisor about a possible thesis
                    // Student offers its own idea for a thesis to supervisor
                    // student and supervisor come to an agreement whether the thesis is OK or not
                    if (adhocThesis !=null){
                        agent.addBehaviour(new ContactSupervisor(agent, researchInterest, adhocThesis));
                    } else {
                        System.out.println("[ERROR] Agent "+agent.getLocalName()+": There are no presented AD HOC thesis proposals.");
                    }

                    break;
                case "PROPOSED":
                    System.out.println("[INFO] Agent"+ agent.getLocalName() + " chose the PROPOSED TH path ");
                    agent.addBehaviour(new RequestThesisProposals(agent, thesisType));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value for thesisType: " + thesisType);
            }
        }else {
            assert agent != null;
            System.out.println("\n[ERROR] thesisType parameter of agent "+agent.getLocalName()+" is null");
        }

    }

}
