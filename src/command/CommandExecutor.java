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


    public void takeCommand(Command c) {
        if (c == null) {
            return;
        }

        commands.add(c);
    }

    public String placeCommand() {
        if (commands.isEmpty()) {
            throw new UnsupportedOperationException("");
            //System.out.println("No command yet taken");
            //return;
        }

        try {
            return commands.get(commands.size() - 1).executeRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new UnsupportedOperationException("");
    }

}
