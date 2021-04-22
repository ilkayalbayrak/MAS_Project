package utils;

import behaviours.*;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.List;
import java.util.Random;

/*
* Utils class is for defining some useful functions that are most likely will be used by
* all agents
* */
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

    // Method for de-registering the agent services to yellow pages
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

        try{
            DFAgentDescription searchTemplate = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(serviceType);
            searchTemplate.addServices(serviceDescription);
//            DFAgentDescription[] results =
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
            System.out.println("[ERROR] There are no agents in yellow pages that offer the service: {"+service+"}.");
            return null;
        }
    }

    public static Thesis pickRandomThesis(List<Thesis> list){
        if(list != null){
            Random rand = new Random();
            Thesis randomElement = list.get(rand.nextInt(list.size()));
            return randomElement;
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

}
