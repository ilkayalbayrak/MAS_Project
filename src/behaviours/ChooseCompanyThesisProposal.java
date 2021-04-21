package behaviours;

import interfaces.enums.ConversationIDs;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import utils.Thesis;
import utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ChooseCompanyThesisProposal extends CyclicBehaviour {
    private Map<AID, List<Thesis>> proposalsByCompany = new HashMap<>();
    private List<Thesis> receivedProposals = new LinkedList<>();

    private final int numberOfCompanies;

    public ChooseCompanyThesisProposal(Agent agent) {
        super(agent);
        this.numberOfCompanies = Utils.getAgentList(myAgent, "company").length;
    }


    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(ConversationIDs.ASK_COMPANY_PROPOSALS.toString()),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);

        if (receivedMessage != null){

            try{
                receivedProposals = (List<Thesis>) receivedMessage.getContentObject();
            } catch (UnreadableException e) {
                System.out.println("[ERROR] Agent:["+ myAgent.getLocalName()+ "] could not extract the proposals object from message");
                e.printStackTrace();
            }

            proposalsByCompany.put(receivedMessage.getSender(), receivedProposals);
            if(numberOfCompanies == proposalsByCompany.size()){
                System.out.println("[INFO] Agent:"+myAgent.getLocalName()+" received a reply from all companies that contains thesis proposals.");

                // todo: Fix random picking by creating a gui

                //randomly pick a company for now
                List<AID> keysAsArray = new ArrayList<>(proposalsByCompany.keySet());
                Random r = new Random();
                AID thesisCompany = keysAsArray.get(r.nextInt(keysAsArray.size()));

                // randomly choose a thesis for now
                Thesis chosenThesis = Utils.pickRandomThesis(proposalsByCompany.get(thesisCompany));
                assert chosenThesis != null;
                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+ "] chose the Thesis:["+chosenThesis.getThesisTitle()+"] within all EXTERNAL proposals.");

                // inform the company about the thesis student wants to choose
                ACLMessage acceptanceMessage = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                acceptanceMessage.addReceiver(thesisCompany);
                acceptanceMessage.setConversationId(ConversationIDs.ACCEPT_COMPANY_THESIS_PROPOSAL.toString());
                try {
                    acceptanceMessage.setContentObject(chosenThesis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("[INFO] Agent:[" +myAgent.getLocalName()+ "] sent the CHOSEN THESIS PROPOSAL:["+chosenThesis.getThesisTitle()+"] to Agent:"+ thesisCompany.getLocalName()+".");
                myAgent.send(acceptanceMessage);

                // after picking one proposal from a company, send rejection messages to other companies to inform them
                ACLMessage rejectionMessage = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                rejectionMessage.setConversationId(ConversationIDs.REJECT_COMPANY_PROPOSAL_IF_NOT_INTERESTED.toString());

                // Add all the rejected companies as receiver
                for(AID company: proposalsByCompany.keySet()){
                    if(!company.equals(thesisCompany)){
                        rejectionMessage.addReceiver(company);
                    }
                }
                System.out.println("[INFO] Agent:[" +myAgent.getLocalName()+ "] informed rest of the companies that it is not interested in their proposals");
                myAgent.send(rejectionMessage);

            } else {
                System.out.println("[ERROR] Number of companies do not match the number of received proposals");
            }
        } else {
            block();
        }

    }
}
