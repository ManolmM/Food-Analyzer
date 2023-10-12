package command;

import exceptions.MissingExtractedDataException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Command {

    /**
     * Executes a request to a RESTful API
     */
    public String executeRequest() throws IOException, InterruptedException, URISyntaxException, MissingExtractedDataException;
}
