package command.rest.get;

import com.google.gson.Gson;
import command.Command;
import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import network.https.properties.Properties;
import storage.databases.ibm_db2.DataExchanger;
import storage.food.nutrients.NutrientCollection;
import storage.syntax.http.request.get.GetFoodCommandSyntax;
import storage.syntax.http.request.get.GetFoodReportCommandSyntax;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;


public class GetFoodReportCommand implements Command {
    private final int fdcIdIndex = 1;

    private final int energyIndex = 0;
    private final int proteinIndex = 1;
    private final int totalLipidsIndex = 2;
    private final int carbohydratesIndex = 3;
    private final int fiberIndex = 4;
    private URI uri;
    private List<String> command;
    private DataExchanger exchanger;

    public GetFoodReportCommand(DataExchanger exchanger, List<String> commandInput) {
        this.exchanger = exchanger;
        this.command = commandInput;
    }

    /**
     * Executes the HTTP GET request to the REST API.
     * Returns human-readable representation about food.
     */
    @Override
    public List<String> execute() throws URISyntaxException, IOException, InterruptedException, MissingExtractedDataException, SQLException {

        FoodByFdcId extractedFoodFromStorage = exchanger.retrieveData(Integer.parseInt(command.get(fdcIdIndex)));
        if (extractedFoodFromStorage != null) {
            return clientOutput(extractedFoodFromStorage);
        }

        HttpRequest request;
        HttpClient client = HttpClient.newBuilder().build();
        Gson gson = new Gson();
        uri = configureUri();
        request = configureRequest(uri);
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            if (jsonResponse.equals("")) {
                throw new MissingExtractedDataException("No such item in the REST API server");
            }
            jsonResponse.replaceAll("'", "");
            FoodByFdcId extractedFood = gson.fromJson(jsonResponse, FoodByFdcId.class);
            FoodByFdcId foodInOrderNutrients = getNewFoodByFdcId(extractedFood);
            exchanger.storeData(foodInOrderNutrients);
            return clientOutput(foodInOrderNutrients);
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Returns URI configured in terms of the REST documentation.
     * @return URI
     */
    private URI configureUri() throws URISyntaxException {
        return new URI(Properties.SCHEME,
                Properties.HOST_FOOD_API,
                configurePath(),
                configureQuery(),
                null);
    }

    private String configurePath() {
        final int fdcIdIndex = 1;
        return GetFoodReportCommandSyntax.PREFIX_PATH_FOOD + command.get(fdcIdIndex);
    }

    /**
     * Returns a query of type: query= + client's input arguments + pageNumber= + queryPage.
     */
    private String configureQuery() {
        StringBuilder query = new StringBuilder();
        return  query.append(GetFoodReportCommandSyntax.ENERGY_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.PROTEIN_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.FAT_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.CARBOHYDRATES_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.FIBER_NUMBER_PARAMETER)
                .toString();
    }

    /**
     * Returns the appropriate HTTP GET request that contains client's query.
     * @return HttpRequest
     */
    private HttpRequest configureRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header(GetFoodReportCommandSyntax.API_KEY_NAME, GetFoodReportCommandSyntax.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }


    /**
     * Transforms the order of the nutrients in the given food.
     */
    private FoodByFdcId getNewFoodByFdcId(FoodByFdcId food) throws MissingExtractedDataException {

        if (food == null) {
            throw new MissingExtractedDataException("FoodByFdcId should not be null");
        }
        // The desired order of the nutrients.
        List<String> nutrients = List.of(NutrientCollection.ENERGY,
                NutrientCollection.PROTEIN,
                NutrientCollection.TOTAL_LIPIDS,
                NutrientCollection.CARBOHYDRATES,
                NutrientCollection.FIBER);

        List<FoodNutrients> newFoodNutrients = new ArrayList<>();
        List<FoodNutrients> shuffledNutrientList = food.foodNutrients();
        int size = shuffledNutrientList.size();

        for (String nutrientName : nutrients) {
            for (int i = 0; i < size; i++) {
                FoodNutrients temp = shuffledNutrientList.get(i);
                if (nutrientName.equals(temp.nutrient().name())) {
                    Nutrient newNutrient = new Nutrient(temp.nutrient().name());
                    FoodNutrients newFood = new FoodNutrients(newNutrient, temp.amount());
                    newFoodNutrients.add(newFood);
                }
            }
        }

        FoodByFdcId newFoodByFdcId = new FoodByFdcId(food.fdcId(), food.description(),
                food.ingredients(), food.gtinUpc(), newFoodNutrients);

        return newFoodByFdcId;



    }


    /**
     * Returns a human-readable information about the food.
     */
    private List<String> clientOutput(FoodByFdcId extractedFood) {
        StringBuilder result = new StringBuilder();
        String temp = extractedFood.description();
        if (temp == null) {
            result.append("Description - No information\n");
        } else {
            result.append("Description - " + temp).append("\n");
        }

        temp = extractedFood.ingredients();
        if (temp == null) {
            result.append("Ingredients - No information\n");
        } else {
            result.append("Ingredients - ").append(temp).append("\n");
        }


        result.append("Nutrients:\n");

        temp = String.valueOf(extractedFood.foodNutrients().get(energyIndex).amount());
        if (temp == null) {
            result.append("Energy - No information");
        } else {
            result.append("Energy - ").append(extractedFood.foodNutrients().get(energyIndex).amount()).append("\n");
        }

        temp = String.valueOf(extractedFood.foodNutrients().get(proteinIndex).amount());
        if (temp == null) {
            result.append("Protein - No information");
        } else {
            result.append("Protein - ").append(extractedFood.foodNutrients().get(proteinIndex).amount()).append("\n");
        }

        temp = String.valueOf(extractedFood.foodNutrients().get(totalLipidsIndex).amount());
        if (temp == null) {
            result.append("Total lipid(fat) - No information");
        } else {
            result.append("Total lipid(fat) - ").append(extractedFood.foodNutrients().get(totalLipidsIndex).amount()).append("\n");
        }

        temp = String.valueOf(extractedFood.foodNutrients().get(carbohydratesIndex).amount());
        if (temp == null) {
            result.append("Carbohydrates - No information");
        } else {
            result.append("Carbohydrates - ").append(extractedFood.foodNutrients().get(carbohydratesIndex).amount()).append("\n");
        }

        temp = String.valueOf(extractedFood.foodNutrients().get(fiberIndex).amount());
        if (temp == null) {
            result.append("Fiber - No information");
        } else {
            result.append("Fiber - ").append(extractedFood.foodNutrients().get(4).amount()).append("\n");
        }

        return List.of(result.toString());
    }
}
