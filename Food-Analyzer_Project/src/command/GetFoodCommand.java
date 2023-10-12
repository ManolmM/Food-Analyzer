package command;

import com.google.gson.Gson;
import json.extractor.food.name.FoodByName;
import json.extractor.page.Page;
import network.https.properties.Properties;
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
    public GetFoodCommand(List<String> commandInput) {

        this.command = commandInput;
    }

    /**
     * Executes a command of type: get-food "branded food name".
     *
     * @return String representing information from the REST API.
     */
    @Override
    public String executeRequest() throws IOException, InterruptedException, URISyntaxException {

        // Sets the HttpRequest and HttpClient instances
        HttpRequest request;
        HttpClient client = HttpClient.newBuilder().build();
        Gson gson = new Gson();

        int pageNumberRequest = 1; // Always starts from page 1
        Page page;
        StringBuilder pageList = new StringBuilder();
        do {
            String newPage = modifyUriQueryPageNumber(pageNumberRequest++);
            uri = configureUri(configureQuery(newPage));
            request = configureRequest(uri);

            String extractedPage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            page = gson.fromJson(extractedPage, Page.class);

            if (page.totalPages() == 0) {  // checks whether the REST API contains any pages with information
                String resultNotFountMessage = "No results are found";
                return resultNotFountMessage;
            }
            pageList.append(modifyPageToClientOutput(page)); // Combines the pages' content into a whole String

        } while (page.totalPages() > page.currentPage());

        return pageList.toString();
    }


    /**
     * Returns the separated client's command to a new String.
     * Omits the type of the command at index zero in our command list.
     * @return String
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
    private URI configureUri(String pageNumber) throws URISyntaxException {
        return new URI(Properties.SCHEME,
                Properties.HOST_FOOD_API,
                GetFoodCommandSyntax.FOODS_PATH,
                pageNumber,
                null);
    }

    /**
     * Returns a query of type: query= + client's input arguments + pageNumber= + queryPage.
     * @return String
     */
    private String configureQuery(String queryPage) {
        return GetFoodCommandSyntax.REQUIRED_API_PARAMETER_QUERY +
                mergeCommandArguments() +
                queryPage +
                GetFoodCommandSyntax.REQUIRED_ALL_WORDS_PARAMETER;
    }


    /**
     * Helper method to modify URI Page
     */
    private String modifyUriQueryPageNumber(int page) {
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
    private HttpRequest configureRequest(URI uri) {
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