package fundstarterserver;

import com.sun.deploy.util.SessionState;
import fundstarter.*;

import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Miguel Prata Leal on 18/10/15.
 */
public class ClientCommand {
    final private static HashMap<String, Integer> commandToNumberOfArgs = new HashMap<String, Integer>() {
        {
            put("login", 2);
        }
    };

    private Connection connection;
    private ServerMessage output;
    private RMIInterface remoteObject;
    private String command;
    private ArrayList<String> arguments;
    private Object attachedObject;
    private ClientSession clientSession;


    public ClientCommand(Connection connection, Command rawCommand, Object attachedObject, ClientSession clientSession) {
        rawCommand.getCommand();
        this.connection = connection;
        this.command = new String(rawCommand.getCommand());
        this.attachedObject = attachedObject;
        this.output = new ServerMessage();
        this.clientSession = clientSession;
        //checkArgsNumber();

        arguments = new ArrayList<String>();

        for (String argument : rawCommand.getArguments())
            arguments.add(argument);

        System.out.println("Command: " + rawCommand.getCommand());

    }

/*
    private void checkArgsNumber() {
        assert commandParsed.length > 0;
        assert commandToNumberOfArgs.get(commandParsed[0]) == (commandParsed.length - 1);
    }
*/


    public void run() {
        remoteObject = LookupRemoteObject();


        switch (command) {
            case "login":
                login();
                break;
            case "signup":
                signUP();
                break;
            case "viewBalance":
                viewBalance();
                break;
            case "viewRewards":
                viewRewards();
                break;
            case "sendReward":
                sendReward();               //TODO Sergio
                break;
            case "listInProgress":
                listInProgress();
                break;
            case "listExpired":
                listExpired();
                break;
            case "newProject":
                newProject();
                break;
            case "pledge":
                pledge();
                break;
            case "viewMessages":
                viewMessages();
                break;
            case "details":
                details();
                break;
            case "sendMessage":
                sendMessage();
                break;
            case "sendMessageToProject":    //TODO Sergio
                sendMessageToProject();
                break;
            case "addAdminToProject":
                addAdminToProject();
                break;
            case "logout":
                logoutUser();
                break;
            case "cancelProject":           //TODO Sergio
                cancelProject();
                break;
            case "addRewardToProject":
                addRewardToProject();
                break;
            case "removeRewardFromProject":
                removeRewardFromProject();
                break;
            case "removeExtraFromProject":
                removeExtraFromProject();
                break;
            case "addExtraToProject":
                addExtraToProject();
                break;
            default:
                assert false;
        }

    }

    private void addExtraToProject() {
        System.out.println("addExtraToProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addExtraReward((Extra) attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Extra added to " + ((Extra) attachedObject).getProjectName()) + ".");
        } else {
            output.setContent(new String("Extra not added. Something went wrong."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void removeExtraFromProject() {
        System.out.println("removeExtraFromProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.removeExtraReward((Extra) attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Extra removed from " + ((Extra) attachedObject).getProjectName()) + ".");
        } else {
            output.setContent(new String("Extra not removed. Something went wrong."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void removeRewardFromProject() {
        System.out.println("removeRewardFromProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.removeReward((Reward) attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Reward removed from " + ((Reward) attachedObject).getProjectName()) + ".");
        } else {
            output.setContent(new String("Reward not removed. Something went wrong."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void addRewardToProject() {
        System.out.println("addRewardToProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addReward((Reward) attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Reward added to " + ((Reward) attachedObject).getProjectName()) + ".");
        } else {
            output.setContent(new String("Reward not added. Something went wrong."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void sendMessageToProject() {
        System.out.println("sendMessageToProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.sendMessageToProject((Message)attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Message sent."));
        } else {
            output.setContent(new String("Message not sent."));
        }
        output.setErrorHappened(!rmiReturnObject);

    }

    private void cancelProject() {
        System.out.println("cancelProject(" + arguments.get(0) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.cancelProject(arguments.get(0), clientSession.getUsernameLoggedIn());
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Project canceled"));
        } else {
            output.setContent(new String("An error has occurred because this project has expired or you don't have enough privileges."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void details() {
        System.out.println("viewProjectDetails(" + clientSession.getUsernameLoggedIn() + ": " +  arguments.get(0) + ")");

        Project rmiReturnObject = null;

        try {

            rmiReturnObject = remoteObject.viewProjectDetails(arguments.get(0));
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject != null) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("That project doesn't exist."));
            output.setErrorHappened(true);

        }
    }

    private void newProject() {
        System.out.println("newProject()");

        Project project = (Project)attachedObject;
        project.setCreator(clientSession.getUsernameLoggedIn());


        boolean rmiReturnObject = false;

        try {

            rmiReturnObject = remoteObject.newProject(project);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Project successfully created."));
        } else {
            output.setContent(new String("Project not created. Something went wrong."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void logoutUser() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": logout()");
        if (clientSession.getSessionLoggedIn() != null) {
            output.setContent("Bye, " + clientSession.getUsernameLoggedIn());
            clientSession.setSessionLoggedIn(false);
            clientSession.setUsernameLoggedIn(null);

        } else {
            output.setContent("Something went wrong. You're already logged out.");
        }
    }


    private void addAdminToProject() {
        System.out.println("addAdminToProject(" + clientSession.getUsernameLoggedIn() + ", " +  arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addAdmin(clientSession.getUsernameLoggedIn(), arguments.get(0), arguments.get(1));
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("The username " + arguments.get(1) + " is now admin of " + arguments.get(0) + "."));
        } else {
            output.setContent(new String("The project wasn't found or you don't have enough privileges to perform this action."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void viewRewards() {
        System.out.println("viewRewards()");
        ArrayList<AttributedReward> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.viewRewards(clientSession.getUsernameLoggedIn());
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("You don't have any rewards.."));
            output.setErrorHappened(true);

        }
    }

    private void sendMessage() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": " + "sendmessage(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        Message message = new Message();
        message.setSendTo(arguments.get(0));
        message.setText(arguments.get(1));
        message.setSendFrom(clientSession.getUsernameLoggedIn());

        try {
            rmiReturnObject = remoteObject.sendMessage(message);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Message sent."));
        } else {
            output.setContent(new String("Message not sent."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }



    private void sendReward() {
        System.out.println("sendReward(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.sendReward(arguments.get(0), arguments.get(1), clientSession.getUsernameLoggedIn(), arguments.get(2));
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Reward successfully sent to " + arguments.get(2)));
        } else {
            output.setContent(new String("Reward not sent."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }


    private void viewBalance() {
        System.out.println("viewBalance()");
        double rmiReturnObject = -1;


        try {
            rmiReturnObject = remoteObject.viewBalance(clientSession.getUsernameLoggedIn());
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject >= 0) {
            output.setContent("Balance: " + rmiReturnObject);
            output.setErrorHappened(false);
        } else {
            output.setContent(new String("Error"));
        }
        output.setErrorHappened(true);
    }

    private void pledge() {
        System.out.println("pledge(" + arguments.get(0) + ", " + arguments.get(1) + ", " + arguments.get(2) + ")");
        boolean rmiReturnObject = false;
        Pledge pledge = new Pledge();
        pledge.setUsername(clientSession.getUsernameLoggedIn());
        pledge.setProjectName(arguments.get(0));
        pledge.setAmount(Double.parseDouble(arguments.get(1)));
        pledge.setAnswer(arguments.get(2));

        try {
            rmiReturnObject = remoteObject.pledge(pledge);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Pledge successful to " + arguments.get(0) + "."));
        } else {
            output.setContent(new String("Not enough credits or project has expired."));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void listExpired() {
        System.out.println("listExpired()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.listExpired();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("Não existem projetos."));
            output.setErrorHappened(true);

        }
    }

    private void listInProgress() {
        System.out.println("listInProgress()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.listInProgress();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("Não existem projetos."));
            output.setErrorHappened(true);

        }
    }


    private RMIInterface LookupRemoteObject() {
        RMIInterface remoteObject = null;

        try {
            //System.getProperties().put("java.security.policy", "policy.all");
            //System.setSecurityManager(new RMISecurityManager());
            remoteObject = (RMIInterface) Naming.lookup("rmi://192.168.1.32:7000/sd");

        } catch (Exception e) {
            output.setContent("Something bad happened to our dataserver: " + e.getMessage());
            output.setErrorHappened(true);
        }
        return remoteObject;
    }



    public void login() {
        System.out.println("login(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.login(arguments.get(0), arguments.get(1));
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Welcome, " + arguments.get(0)));
            clientSession.setUsernameLoggedIn(arguments.get(0));
        } else {
            output.setContent(new String("Invalid username or password."));
        }
        clientSession.setSessionLoggedIn(true);
        output.setErrorHappened(!rmiReturnObject);
    }

    public void signUP() {
        System.out.println("signup(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.signUp(arguments.get(0), arguments.get(1));
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (rmiReturnObject) {
            output.setContent(new String("User, " + arguments.get(0) + " registered."));
        } else {
            output.setContent(new String("Username already exists"));
        }
        clientSession.setSessionLoggedIn(false);
        output.setErrorHappened(!rmiReturnObject);
    }

    private void viewMessages() {
        System.out.println("viewMessages()");
        ArrayList<Message> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.viewMessages(clientSession.getUsernameLoggedIn());
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("Não existem mensagens."));
            output.setErrorHappened(true);

        }
    }





    public ServerMessage getServerMessage() {
        return new ServerMessage(output);
    }

}
