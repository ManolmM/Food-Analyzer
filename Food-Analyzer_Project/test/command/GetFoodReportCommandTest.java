package command;

import exceptions.MissingCommandArgumentsException;
import exceptions.NoCommandProvidedException;
import exceptions.UnknownCommandException;
import org.junit.Test;
import storage.databases.ibm_db2.DataExchanger;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class GetFoodReportCommandTest {

    private DataExchanger exchanger;
    private String clientInput;
    @Test
    public void testExecuteRequest() throws IOException, MissingCommandArgumentsException, UnknownCommandException, NoCommandProvidedException {
        clientInput = "get-food-report 2110388";  // A valid command
        assertNotNull(CommandCreator.newCommand(exchanger, clientInput));
    }

}
