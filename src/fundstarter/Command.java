package fundstarter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xavier on 25-10-2015.
 */
public class Command implements Serializable{
    private static final long serialVersionUID = 1L;
    private String command;
    private ArrayList<String> arguments;
    private Object attachedObject;


    public Command(String command, ArrayList<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Command() {
        this.command = "";
        this.arguments = new ArrayList<>();
    }


    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    public Object getAttachedObject() {
        return attachedObject;
    }

    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", arguments=" + arguments +
                ", attachedObject=" + attachedObject +
                '}';
    }
}
