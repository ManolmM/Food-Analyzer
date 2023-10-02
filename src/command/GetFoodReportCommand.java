package command;

import network.http.handler.HttpService;

import java.util.List;

public class GetFoodReportCommand implements Command {

    private List<String> command;
    private HttpService service;
    public GetFoodReportCommand(HttpService service, List<String> commandInput) {
        this.service = service;
        this.command = commandInput;
    }
    @Override
    public void executeRequest() {

    }
}
