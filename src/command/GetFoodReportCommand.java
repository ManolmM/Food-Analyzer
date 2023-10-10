package command;

import com.google.gson.Gson;
import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import network.https.properties.Properties;
import storage.foods.DataExchanger;
import storage.foods.nutrients.NutrientCollection;
import storage.syntax.http.request.get.GetFoodReportCommandSyntax;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    @Override
    public String executeRequest() throws URISyntaxException, IOException, InterruptedException, MissingExtractedDataException {

        FoodByFdcId extractedFoodFromStorage = exchanger.retrieveData(Integer.parseInt(command.get(fdcIdIndex)));
        if (extractedFoodFromStorage != null) {
            return clientOutput(extractedFoodFromStorage);
        }

        HttpRequest request;
        HttpClient client = HttpClient.newBuilder().build();
        Gson gson = new Gson();
        uri = configureUri();
        request = configureRequest(uri);

        String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        FoodByFdcId extractedFood = gson.fromJson(jsonResponse, FoodByFdcId.class);
        FoodByFdcId foodInOrderNutrients = getNewFoodByFdcId(extractedFood);
        try {
            exchanger.storeData(foodInOrderNutrients);
            return clientOutput(foodInOrderNutrients);
        } catch (MissingExtractedDataException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

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

    private String configureQuery() {
        StringBuilder query = new StringBuilder();
        return  query.append(GetFoodReportCommandSyntax.ENERGY_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.PROTEIN_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.FAT_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.CARBOHYDRATES_NUMBER_PARAMETER).append("&")
                .append(GetFoodReportCommandSyntax.FIBER_NUMBER_PARAMETER)
                .toString();
    }
    private HttpRequest configureRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header(GetFoodReportCommandSyntax.API_KEY_NAME, GetFoodReportCommandSyntax.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }


    private FoodByFdcId getNewFoodByFdcId(FoodByFdcId food) {
        List<String> nutrients = List.of(NutrientCollection.ENERGY,
                NutrientCollection.PROTEIN,
                NutrientCollection.TOTAL_LIPIDS,
                NutrientCollection.CARBOHYDRATES,
                NutrientCollection.FIBER); // contains the nutrients in the right order

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


    private String clientOutput(FoodByFdcId extractedFood) {
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
            result.append(temp).append("\n");
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

        return result.toString();
    }
}
