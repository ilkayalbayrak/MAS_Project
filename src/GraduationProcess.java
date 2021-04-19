
import agents.*;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;



public class GraduationProcess {
    AgentController rma;
    AgentController sniffer;

    AgentController student1;
    AgentController student2;
    AgentController student3;
    AgentController student4;
    AgentController student5;

    AgentController supervisor1;
    AgentController supervisor2;
    AgentController supervisor3;
    AgentController supervisor4;
    AgentController supervisor5;

    AgentController reviewer1;
    AgentController reviewer2;
    AgentController reviewer3;
    AgentController reviewer4;
    AgentController reviewer5;

    AgentController company;
    AgentController thesisCommittee;



    public static void main(String[] args) {
        GraduationProcess graduationProcess = new GraduationProcess();

        Runtime rt = Runtime.instance();

        // Launch a complete platform on the 8888 port
        // create a default Profile
        Profile pMain = new ProfileImpl("localhost", 8888, null);

        System.out.println("Launching a whole in-process platform..." + pMain);
        AgentContainer mc = rt.createMainContainer(pMain);

        // Set the default Profile to start a container
//        ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
//        System.out.println("Launching the agent container ..." + pContainer);

        System.out.println("Launching the rma agent on the main container ...");
//        AgentController rma = null;
        try {
            // Create agents

            // Start rma as a regular agent instead of using the console
            graduationProcess.rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);

            // Create student agents
            graduationProcess.student1 = mc.createNewAgent("Student1", Student1.class.getName(), new Object[]{"PROPOSED", "MACHINE_LEARNING"});
            graduationProcess.student2 = mc.createNewAgent("Student2", Student2.class.getName(), new Object[]{"EXTERNAL", "MACHINE_LEARNING"});
            graduationProcess.student3 = mc.createNewAgent("Student3", Student3.class.getName(), new Object[]{"AD_HOC", "NATURAL_LANGUAGE_PROCESSING"});
            graduationProcess.student4 = mc.createNewAgent("Student4", Student4.class.getName(), new Object[]{"AD_HOC", "MULTI_AGENT_SYSTEMS"});
            graduationProcess.student5 = mc.createNewAgent("Student5", Student5.class.getName(), new Object[]{"PROPOSED", "COMPUTER_VISION"});

            //Create supervisors agents
            graduationProcess.supervisor1 = mc.createNewAgent("Supervisor1", Supervisor1.class.getName(), null);
            graduationProcess.supervisor2 = mc.createNewAgent("Supervisor2", Supervisor2.class.getName(), null);
            graduationProcess.supervisor3 = mc.createNewAgent("Supervisor3", Supervisor3.class.getName(), null);
            graduationProcess.supervisor4 = mc.createNewAgent("Supervisor4", Supervisor4.class.getName(), null);
            graduationProcess.supervisor5 = mc.createNewAgent("Supervisor5", Supervisor5.class.getName(), null);

            // Create reviewer agents
            graduationProcess.reviewer1 = mc.createNewAgent("Reviewer1", Reviewer1.class.getName(), null);
            graduationProcess.reviewer2 = mc.createNewAgent("Reviewer2", Reviewer2.class.getName(), null);
            graduationProcess.reviewer3 = mc.createNewAgent("Reviewer3", Reviewer3.class.getName(), null);
            graduationProcess.reviewer4 = mc.createNewAgent("Reviewer4", Reviewer4.class.getName(), null);
            graduationProcess.reviewer5 = mc.createNewAgent("Reviewer5", Reviewer5.class.getName(), null);

//            graduationProcess.reviewer1.
            // Create thesisCommittee agent
            graduationProcess.thesisCommittee = mc.createNewAgent("ThesisCommittee", ThesisCommittee.class.getName(), null);

            // Create company agent
            graduationProcess.company = mc.createNewAgent("Company", Company.class.getName(), null);


            // Start agents
            graduationProcess.rma.start();

            graduationProcess.student1.start();
            graduationProcess.student2.start();
            graduationProcess.student3.start();
            graduationProcess.student4.start();
            graduationProcess.student5.start();

            graduationProcess.supervisor1.start();
            graduationProcess.supervisor2.start();
            graduationProcess.supervisor3.start();
            graduationProcess.supervisor4.start();
            graduationProcess.supervisor5.start();

            graduationProcess.reviewer1.start();
            graduationProcess.reviewer2.start();
            graduationProcess.reviewer3.start();
            graduationProcess.reviewer4.start();
            graduationProcess.reviewer5.start();

            graduationProcess.thesisCommittee.start();

            graduationProcess.company.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }


    }
}
