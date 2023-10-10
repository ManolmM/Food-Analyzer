package command;

import java.util.LinkedList;
import java.util.List;

public class CommandExecutor {

    List<Command> commands;

    private CommandExecutor() {
        commands = new LinkedList<>();
    }

    public static CommandExecutor newInstance() {
        return new CommandExecutor();
    }


    /**
     * Stores the commands passed
     *
     **/
    public void takeCommand(Command c) {
        if (c == null) {
            return;
        }

        commands.add(c);
    }


    /**
     * Executes the latest command added.
     *
     * @return modified response information from the REST API
     * @throws
     **/
    public String placeCommand() {
        if (commands.isEmpty()) {
            throw new IllegalStateException("Trying to execute empty list of commands");
        }

        try {
            return commands.get(commands.size() - 1).executeRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Unable to execute latest command");
    }

}
