package command;

import exceptions.MissingCommandArgumentsException;
import exceptions.NoCommandProvidedException;
import exceptions.UnknownCommandException;
import org.junit.Test;
import storage.foods.DataExchanger;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CommandCreatorTest {

    private  DataExchanger exchanger;
    private  String clientInput;

    @Test
    public void testNewCommandShouldReturnInstanceOfCommand() throws MissingCommandArgumentsException, UnknownCommandException, IOException, NoCommandProvidedException {
        clientInput = "get-food Raffaello treat";
        Command newCommand = CommandCreator.newCommand(exchanger, clientInput);
        assertTrue( newCommand instanceof Command);

    }

    @Test
    public void testNewCommandShouldReturnGetFoodCommandInstance() throws MissingCommandArgumentsException, UnknownCommandException, IOException, NoCommandProvidedException {
        clientInput = "get-food Raffaello treat";
        Command getFoodCommand = CommandCreator.newCommand(exchanger, clientInput);
        assertTrue(getFoodCommand instanceof GetFoodCommand);
    }
    @Test
    public void testNewCommandShouldThrowExceptionWithUnknownCommandType() {
        clientInput = "get-foodd Raffaello treat";
        assertThrows("Command: get-foodd should throws exception.",
                UnknownCommandException.class, () -> CommandCreator.newCommand(exchanger, clientInput));

        clientInput = "get-food-repor 2032440";
        assertThrows("Command: get-food-repor should throws exception.",
                UnknownCommandException.class, () -> CommandCreator.newCommand(exchanger, clientInput));
    }
    @Test
    public void testNewCommandShouldReturnGetFoodReportCommandInstance() throws MissingCommandArgumentsException, UnknownCommandException, IOException, NoCommandProvidedException {
        clientInput = "get-food-report 2032440";
        Command newCommand = CommandCreator.newCommand(exchanger, clientInput);
        assertTrue(newCommand instanceof GetFoodReportCommand);
    }

    @Test
    public void testNewCommandShouldThrowExceptionWithEmptyCommand() {
        clientInput = "";
        assertThrows("Empty command should throws exception.",
                NoCommandProvidedException.class, () -> CommandCreator.newCommand(exchanger, clientInput));
    }

    @Test
    public void testNewCommandShouldThrowExceptionWithBlack() {
        clientInput = "     ";
        assertThrows("Blank command should throws exception.",
                NoCommandProvidedException.class, () -> CommandCreator.newCommand(exchanger, clientInput));
    }

    @Test
    public void testNewCommandShouldThrowExceptionWithNullCommand() {
        clientInput = null;
        assertThrows("Null command should throws exception.",
                NoCommandProvidedException.class, () -> CommandCreator.newCommand(exchanger, clientInput));
    }

    @Test
    public void testNewCommandShouldThrowExceptionWitMissingArguments() {
        clientInput = "get-food ";
        assertThrows("Command with no arguments should throw exception.",
                MissingCommandArgumentsException.class, () -> CommandCreator.newCommand(exchanger, clientInput));

        clientInput = "get-food";
        assertThrows("Null command should throw exception.",
                MissingCommandArgumentsException.class, () -> CommandCreator.newCommand(exchanger, clientInput));

        clientInput = "get-food-report ";
        assertThrows("Null command should throw exception.",
                MissingCommandArgumentsException.class, () -> CommandCreator.newCommand(exchanger, clientInput));

        clientInput = "get-food-report";
        assertThrows("Null command should throw exception.",
                MissingCommandArgumentsException.class, () -> CommandCreator.newCommand(exchanger, clientInput));
    }


}
