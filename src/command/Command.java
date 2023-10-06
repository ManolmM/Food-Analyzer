package command;

import exceptions.MissingExtractedDataException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Command {
    void executeRequest() throws IOException, InterruptedException, URISyntaxException, MissingExtractedDataException;
}
