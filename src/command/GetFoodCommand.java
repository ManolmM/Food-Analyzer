package command;

import com.google.gson.Gson;
import json.extractor.food.name.FoodByName;
import json.extractor.page.Page;
import network.http.handler.HttpHandler;
import storage.foods.DataExchanger;
import storage.syntax.http.request.get.GetFoodCommandSyntax;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GetFoodCommand implements Command {
    private URI uri;
    private List<String> command;
    private DataExchanger exchanger;
    public GetFoodCommand(DataExchanger exchanger, List<String> commandInput) {
        this.exchanger = exchanger;
        this.command = commandInput;
    }
    @Override
    public String executeRequest() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request;
        HttpClient client = HttpClient.newBuilder().build();
        Gson gson = new Gson();

        int pageNumberRequest = 1;
        Page page;
        StringBuilder pageList = new StringBuilder();
        do {
            String newPage = modifyUriQueryPageNumber(pageNumberRequest++);
            uri = configureUri(configureQuery(newPage));
            request = configureRequest(uri);

            String extractedPage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            page = gson.fromJson(extractedPage, Page.class);

            if (page.totalPages() == 0) {
                String resultNotFountMessage = "No results are found";
                return resultNotFountMessage;
            }
            pageList.append(modifyPageToClientOutput(page));

        } while (page.totalPages() > page.currentPage());

        return pageList.toString();
    }

    private String mergeCommandArguments() {
        StringBuilder argsBuilder = new StringBuilder();

        final int startingIndex = 1;
        List<String> subList = command.subList(startingIndex, command.size());
        argsBuilder.append(String.join(" ", subList));
        argsBuilder.append("&");
        return argsBuilder.toString();
    }

    private URI configureUri(String pageNumber) throws URISyntaxException {
        return new URI(HttpHandler.SCHEME,
                HttpHandler.HOST_FOOD_API,
                GetFoodCommandSyntax.FOODS_PATH,
                pageNumber,
                null);
    }

    private String configureQuery(String queryPage) {
        return GetFoodCommandSyntax.REQUIRED_API_PARAMETER_QUERY +
                mergeCommandArguments() +
                queryPage +
                GetFoodCommandSyntax.REQUIRED_ALL_WORDS_PARAMETER;
    }
    private String modifyUriQueryPageNumber(int page) {
        StringBuilder buildPage = new StringBuilder();
        buildPage.append(GetFoodCommandSyntax.PAGE_NUMBER);
        buildPage.append(page);
        buildPage.append("&");

        return buildPage.toString();
    }

    private HttpRequest configureRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header(GetFoodCommandSyntax.API_KEY_NAME, GetFoodCommandSyntax.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }

    private String modifyPageToClientOutput(Page page) {
        StringBuilder output = new StringBuilder();

        for (FoodByName item : page.foods()) {
            output.append("FdcId - ").append(item.fdcId()).append(", ")
                    .append("Description - ").append(item.description()).append(", ");
            if (item.gtinUpc() == null) {
                output.append("GtinUpc - No information\n");
                continue;
            }
            output.append("GtinUpc - ").append(item.gtinUpc()).append("\n");
        }

        return output.toString();
    }

}
