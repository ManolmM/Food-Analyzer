package command;

import exceptions.MissingCommandArgumentsException;
import exceptions.NoCommandProvidedException;
import exceptions.UnknownCommandException;
import org.junit.Test;
import storage.foods.DataExchanger;

import java.io.IOException;
import java.net.URISyntaxException;

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
