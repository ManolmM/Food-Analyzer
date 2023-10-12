package command;

import command.type.CommandType;
import exceptions.NoCommandProvidedException;
import exceptions.MissingCommandArgumentsException;
import exceptions.UnknownCommandException;
import storage.foods.DataExchanger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandCreator {

    private final static int COMMAND_TYPE_INDEX = 0;

    /**
     * Checks if the client's input command is correctly supplied.
     *
     * @return List of String representing the command.
     **/
    private static List<String> getCommandArguments(String clientInput) throws NoCommandProvidedException,
                                                                               UnknownCommandException,
                                                                               MissingCommandArgumentsException {
        if (clientInput == null || clientInput.isEmpty() || clientInput.isBlank()) {
            throw new NoCommandProvidedException("No command is provided");
        }

        String[] splitCommand = clientInput.split(" ");

        if (getType(splitCommand[COMMAND_TYPE_INDEX]) == CommandType.UNKNOWN) {
            throw new UnknownCommandException("Invalid command input");
        }

        if (splitCommand.length == 1) {
            throw new MissingCommandArgumentsException("No arguments provided in command \"" + splitCommand[0] + "\"");
        }

        List<String> commandArguments = new ArrayList<>(List.of(splitCommand));
        return commandArguments;
    }

    /**
     * Checks if the client's input command is correctly supplied.
     *
     * @return CommandType
     **/
    private static CommandType getType(String commandType) {
        return switch (commandType) {
            case "get-food" -> CommandType.GET_FOOD;
            case "get-food-report" -> CommandType.GET_FOOD_REPORT;
            default -> CommandType.UNKNOWN;
        };
    }

    /**
     * Creates new command from given client's input.
     *
     * @return Command of specified type
     **/
    public static Command newCommand(DataExchanger dataExchanger, String clientInput) throws NoCommandProvidedException,
                                                                UnknownCommandException,
                                                                MissingCommandArgumentsException,
                                                                IOException {

        List<String> args = getCommandArguments(clientInput);
        CommandType type = getType(args.get(COMMAND_TYPE_INDEX));
        return switch (type) {
            case GET_FOOD -> new GetFoodCommand(args);
            default -> new GetFoodReportCommand(dataExchanger, args);
        };
    }

}
