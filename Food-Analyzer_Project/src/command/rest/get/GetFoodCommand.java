package command.rest.get;

import com.google.gson.Gson;
import command.Command;
import json.extractor.food.name.FoodByName;
import json.extractor.page.Page;
import json.extractor.page.PageList;
import network.https.properties.Properties;
import storage.syntax.http.request.get.GetFoodCommandSyntax;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class GetFoodCommand implements Command {
    private HttpRequest request;
    private HttpClient client;
    private Gson gson;
    private URI uri;
    private List<String> command;
    private ExecutorService requestExecutor;
    public GetFoodCommand(List<String> commandInput, ExecutorService executorService) {
        this.command = commandInput;
        client = HttpClient.newBuilder().build();
        gson = new Gson();
        requestExecutor = executorService;
    }

    /**
     * Executes a command of type: get-food "branded food name".
     *
     * @return String representing information from the REST API.
     */
    @Override
    public List<String> execute() throws IOException, InterruptedException, URISyntaxException {
        PageList page;
        uri = configureUri(configureQueryList());
        request = configureRequest(uri);

        String extractedPage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        page = gson.fromJson(extractedPage, PageList.class);

        if (page.totalPages() == 0) {  // checks whether the REST API contains any pages with information
            List<String> resultNotFountMessage = List.of("No results are found");
            return resultNotFountMessage;
        }
        return resultPages(page.totalPages());
    }

    /**
     * Returns the separated client's command to a new String.
     * Omits the type of the command at index zero in our command list.
     */
    private String mergeCommandArguments() {
        StringBuilder argsBuilder = new StringBuilder();

        final int startingIndex = 1;
        List<String> subList = command.subList(startingIndex, command.size());
        argsBuilder.append(String.join(" ", subList));
        argsBuilder.append("&");
        return argsBuilder.toString();
    }


    /**
     * Returns URI configured in terms of the REST documentation.
     * @return URI
     */
    private synchronized URI configureUri(String pageNumber) throws URISyntaxException {
        return new URI(Properties.SCHEME,
                Properties.HOST_FOOD_API,
                GetFoodCommandSyntax.FOODS_PATH,
                pageNumber,
                null);
    }

    /**
     * Returns a query of type: query= + client's input arguments + pageNumber= + queryPage.
     */
    private synchronized String configureQuery(String queryPage) {
        return GetFoodCommandSyntax.REQUIRED_API_PARAMETER_QUERY +
                mergeCommandArguments() +
                //queryPage +
                GetFoodCommandSyntax.REQUIRED_ALL_WORDS_PARAMETER;
    }

    /**
     * Returns a query of type: query= + client's input arguments + dataType= .
     */
    private String configureQueryList() {
        return GetFoodCommandSyntax.REQUIRED_API_PARAMETER_QUERY +
                mergeCommandArguments() +
                GetFoodCommandSyntax.DATA_TYPE_BRANDED +
                GetFoodCommandSyntax.REQUIRED_ALL_WORDS_PARAMETER;
    }


    /**
     * Helper method to modify URI Page
     */
    private synchronized String modifyUriQueryPageNumber(int page) {
        StringBuilder buildPage = new StringBuilder();
        buildPage.append(GetFoodCommandSyntax.PAGE_NUMBER);
        buildPage.append(page);
        buildPage.append("&");

        return buildPage.toString();
    }


    /**
     * Returns the appropriate HTTP GET request that contains client's query.
     * @return HttpRequest
     */
    private synchronized HttpRequest configureRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header(GetFoodCommandSyntax.API_KEY_NAME, GetFoodCommandSyntax.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }

    /**
     * Returns the modified page from the REST API.
     * The content is presented in a human-readable format that is sent to the client from our server.
     * @return String
     * */
    private synchronized String modifyPageToClientOutput(Page page) {
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

    private List<String> resultPages(int totalPages) throws InterruptedException {
        AtomicInteger currentPage = new AtomicInteger(1);
        List<String> pageList = Collections.synchronizedList(new ArrayList<>());

        List<Callable<String>> tasks = new ArrayList<>();

        for (int i = 1; i <= totalPages; i++) {

            Callable<String> task = () -> {
                Page page;
                String newPage = modifyUriQueryPageNumber(currentPage.get());
                try {
                    uri = configureUri(configureQuery(newPage));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                request = configureRequest(uri);

                String extractedPage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                page = gson.fromJson(extractedPage, Page.class);
                synchronized (pageList) {
                    pageList.add((modifyPageToClientOutput(page)));
                }

                return null;
            };
            tasks.add(task);
            currentPage.incrementAndGet();
        }
        requestExecutor.invokeAll(tasks);
        return Collections.unmodifiableList(pageList);
    }

}
