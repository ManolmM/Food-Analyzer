package command;

import exceptions.MissingExtractedDataException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
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
            throw new IllegalArgumentException("Command should be non-nullable");
        }

        commands.add(c);
    }


    /**
     * Executes the latest added command.
     *
     * @return modified response information from the REST API
     * @throws
     **/
    public List<String> placeCommand() throws IOException, URISyntaxException, InterruptedException, MissingExtractedDataException {
        if (commands.isEmpty()) {
            throw new IllegalStateException("Trying to execute empty list of commands");
        }

         int latestCommandIndex = commands.size() - 1;
         int commandToBeRemovedIndex = latestCommandIndex - 1;  // Previous command's index to be removed.
         if (latestCommandIndex > 0) {
             commands.remove(commandToBeRemovedIndex);
             latestCommandIndex -= 1;   // Commands are shifted, so we have to decrement the index with one.
         }

         return commands.get(latestCommandIndex).execute();
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

}
