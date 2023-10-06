package command;

import command.type.CommandType;
import exceptions.EmptyCommandException;
import exceptions.MissingCommandArgumentsException;
import exceptions.UnknownCommandException;
import network.http.handler.HttpHandler;
import storage.foods.DataExchanger;
import storage.foods.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandCreator {

    private final static int COMMAND_TYPE_INDEX = 0;

    /**
     * Checks the correctness of client's command.
     *
     * @return List of String representing the command
     **/
    private static List<String> getCommandArguments(String clientInput) throws EmptyCommandException,
                                                                               UnknownCommandException,
                                                                               MissingCommandArgumentsException {
        if (clientInput == null || clientInput.isEmpty() || clientInput.isBlank()) {
            throw new EmptyCommandException("No command is provided");
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


    private static CommandType getType(String commandType) {
        return switch (commandType) {
            case "get-food" -> CommandType.GET_FOOD;
            case "get-food-report" -> CommandType.GET_FOOD_REPORT;
            case "get-food-by-barcode" -> CommandType.GET_FOOD_BY_BARCODE;
            default -> CommandType.UNKNOWN;
        };
    }

    /**
     * Creates new command from given client's input.
     *
     * @return new Command type
     **/
    public static Command newCommand(String clientInput) throws EmptyCommandException,
                                                                UnknownCommandException,
                                                                MissingCommandArgumentsException,
                                                                IOException {

        List<String> args = getCommandArguments(clientInput);
        CommandType type = getType(args.get(COMMAND_TYPE_INDEX));
        return switch (type) {
            case GET_FOOD -> new GetFoodCommand(DataExchanger.of(FileHandler.newInstance()), args);
            case GET_FOOD_REPORT -> new GetFoodReportCommand(DataExchanger.of(FileHandler.newInstance()), args);
            default -> new GetFoodByBarcodeCommand(DataExchanger.of(FileHandler.newInstance()), args);
        };
    }

}
