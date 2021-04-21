package behaviours;

//import interfaces.ProfessorMessageContents;
import interfaces.enums.ConversationIDs;
import interfaces.enums.ProfessorMessageContents;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/*
* Student behaviour that waits for a message from the Thesis committee which says
* if the EXTERNAL thesis proposal rejected or accepted
* if rejected starts RequestExternalThesisProposals behaviour that searches
* for EXTERNAL proposals
* */
public class ListenIfExternalProposalSufficient extends CyclicBehaviour {
    public ListenIfExternalProposalSufficient(Agent agent) {
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        reset();
//    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationIDs.INFORM_STUDENT_IF_EXTERNAL_PROPOSAL_SUFFICIENT.toString());
        ACLMessage receivedMessage = myAgent.receive(messageTemplate);
        if (receivedMessage != null){

            if (receivedMessage.getPerformative() == ACLMessage.REJECT_PROPOSAL){

                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] will choose a new thesis, since the the initial one rejected because it was insufficient");
                myAgent.addBehaviour(new RequestExternalThesisProposals(myAgent));

            }else if(receivedMessage.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
                System.out.println("[INFO] Agent:["+myAgent.getLocalName()+"] given the green light for its EXTERNAL thesis proposal by the Thesis Committee");
            } else{
                System.out.println("[ERROR] ListenIfExternalProposalSufficient behaviour received a message with incorrect PERFORMATIVE! ");
            }

        }else{
            block();
        }
    }
}
