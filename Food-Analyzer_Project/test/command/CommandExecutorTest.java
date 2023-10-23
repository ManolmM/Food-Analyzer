/*
package command;

import command.rest.get.GetFoodCommand;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import storage.databases.ibm_db2.DataExchanger;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class CommandExecutorTest {

    private String args;
    @Mock
    private DataExchanger dataExchangerMock;
    @InjectMocks
    private Command command;
    @Mock
    private CommandExecutor executor = CommandExecutor.newInstance();

//    @InjectMocks
//    private static Command newCommand = CommandCreator.newCommand(dataExchangerMock, args);
    @Test
    public void testTakeCommandNullCommand() {
        assertTrue("No commands added at the beginning.",executor.getCommands().isEmpty());
        Command newCommand = null;
        assertThrows("Null commands are not allowed for storage.",IllegalArgumentException.class, () -> executor.takeCommand(newCommand));

    }

    @Test
    public void testTakeCommandNonNullableCommand() {

        List<String> commandList = List.of("get-food", "chocolate", "waffle");
        Command newCommand = new GetFoodCommand(commandList);

        assertTrue("No commands added at the beginning.",executor.getCommands().isEmpty());
        executor.takeCommand(newCommand);

        int expectedStorageSize = 1;
        int actualStorageSize = executor.getCommands().size();
        assertEquals(expectedStorageSize, actualStorageSize);
    }

    @Test
    public void testTakeCommandNonNullableCommandAtStorageWithOneCommand() {
        int expectedStorageSize;
        int actualStorageSize;
        List<String> commandList = List.of("get-food","chocolate", "waffle");
        Command newCommand = new GetFoodCommand(commandList);

        testTakeCommandNonNullableCommand();  // Add the first command.
        executor.takeCommand(newCommand);     // Add the second command.

        expectedStorageSize = 2;
        actualStorageSize = executor.getCommands().size();
        assertEquals(expectedStorageSize, actualStorageSize);
    }

    @Test
    public void testPlaceCommandAtEmptyCommandStorageShouldThrowException() {
        assertTrue("Command storage should be empty at first.", executor.getCommands().isEmpty());
        assertThrows("Trying to execute non-existent command is forbidden.", IllegalStateException.class, () -> executor.placeCommand());
    }

    @Test
    public void testPlaceCommandAtEmptyCommandStorageShouldReturnNonNullableClientOutput() {
        assertTrue("Command storage should be empty at first.", executor.getCommands().isEmpty());
        testTakeCommandNonNullableCommand(); // Add a command
        assertNotNull(executor.placeCommand());

        int expectedStorageSize = 1;
        int actualStorageSize = executor.getCommands().size();
        assertEquals(expectedStorageSize, actualStorageSize);
    }


    @Test
    public void testPlaceCommandTwoConsecutiveCommandsShouldReturnNonNullableClientOutput() {
        assertTrue("Command storage should be empty at first.", executor.getCommands().isEmpty());
        testTakeCommandNonNullableCommand();    // Add the first command.
        assertNotNull(executor.placeCommand());

        List<String> commandList = List.of("get-food", "Raffaello", "treat");
        Command newCommand = new GetFoodCommand(commandList);

        executor.takeCommand(newCommand); // Adds the second command.
        executor.placeCommand();
        int expectedStorageSize = 1;      // Before executing the latest command, the previous is being removed.
        int actualStorageSize = executor.getCommands().size();
        assertEquals(expectedStorageSize, actualStorageSize);
    }



}
*/
