package command;

import exceptions.MissingCommandArgumentsException;
import exceptions.NoCommandProvidedException;
import exceptions.UnknownCommandException;
import org.junit.Test;
import storage.foods.DataExchanger;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class GetFoodCommandTest {


    private DataExchanger exchanger;
    private String clientInput;
    @Test
    public void testExecuteRequest() throws IOException, InterruptedException, URISyntaxException, MissingCommandArgumentsException, UnknownCommandException, NoCommandProvidedException {
        clientInput = "get-food Raffaello treat";  // A valid command
        assertNotNull(CommandCreator.newCommand(exchanger, clientInput));
    }
}
