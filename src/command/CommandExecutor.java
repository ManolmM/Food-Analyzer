package command;

import network.http.handler.HttpHandler;

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

    public void placeCommand() {
        if (commands.isEmpty()) {
            System.out.println("No command yet taken");
            return;
        }

        try {
            commands.get(commands.size() - 1).executeRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
