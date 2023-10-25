package command;

import command.queries.InsertQuery;
import command.rest.get.GetFoodCommand;
import exceptions.MissingCommandArgumentsException;
import exceptions.NoCommandProvidedException;
import exceptions.UnknownCommandException;
import org.junit.Test;
import storage.databases.ibm_db2.DataExchanger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetFoodCommandTest {


    private DataExchanger exchanger;
    private String clientInput;

    @Test
    public void setUp() throws SQLException, IOException {
        exchanger = DataExchanger.of(new InsertQuery());
    }

    @Test
    public void testExecuteRequestShouldNotAdjustStorageSize() {

        try {
            setUp();
            clientInput = "get-food Raffaello treat";  // A valid command.
            Command newCommand = CommandCreator.newCommand(exchanger, clientInput); // Creates a new GetFoodCommand.

            int storageSize = exchanger.getStorage().size();
            newCommand.execute();

            int expectedStorageSize = storageSize; // After executing a GetFoodCommand the size should not be changed.
            assertEquals(expectedStorageSize, exchanger.getStorage().size());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }



}
