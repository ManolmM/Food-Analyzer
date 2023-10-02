package command;

import network.http.handler.HttpService;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Command {
    void executeRequest() throws IOException, InterruptedException, URISyntaxException;
}
