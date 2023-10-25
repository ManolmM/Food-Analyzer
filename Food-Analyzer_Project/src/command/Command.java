package command;

import exceptions.MissingExtractedDataException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public interface Command {

    /**
     * Executes a request to a RESTful API
     */
     List<String> execute() throws IOException, InterruptedException, URISyntaxException, MissingExtractedDataException, SQLException;
}
