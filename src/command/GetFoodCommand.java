package command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import json.FoodExtractor;
import json.PageExtractor;
import network.http.handler.HttpService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GetFoodCommand implements Command {

    private final String requiredApiParameter = "query=";
    private final String requiredAllWordsParameter = "requireAllWords=true&";
    private URI uri;
    private List<String> command;
    private HttpService service;
    public GetFoodCommand(HttpService service, List<String> commandInput) {
        this.service = service;
        this.command = commandInput;
    }
    @Override
    public void executeRequest() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = service.getRequest();
        HttpClient client = service.getClient();
        Gson gson = new Gson();

        int pageNumberRequest = 1;
        PageExtractor page;
        do {
            String newPage = modifyUriQueryPageNumber(pageNumberRequest++);
            uri = configureUri(configureQuery(newPage));
            request = configureRequest(uri);

            String extractedPage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            page = gson.fromJson(extractedPage, PageExtractor.class);

            for (FoodExtractor item : page.foods()) {
                System.out.println(item.fdcId() + " " + item.description() + " " + item.gtinUpc());
            }

        } while (page.totalPages() > page.currentPage());


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
        return new URI(HttpService.SCHEME,
                HttpService.HOST,
                HttpService.PREFIX_PATH,
                pageNumber,
                null);
    }

    private String configureQuery(String queryPge) {
        return requiredApiParameter + mergeCommandArguments() + queryPge + requiredAllWordsParameter;
    }
    private String modifyUriQueryPageNumber(int page) {
        StringBuilder buildPage = new StringBuilder();
        buildPage.append(HttpService.PAGE_NUMBER);
        buildPage.append(page);
        buildPage.append("&");

        return buildPage.toString();
    }

    private HttpRequest configureRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header(HttpService.API_KEY_NAME, HttpService.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }
}
