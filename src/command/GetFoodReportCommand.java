package command;

import com.google.gson.Gson;
import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import network.http.handler.HttpHandler;
import storage.foods.DataExchanger;
import storage.syntax.http.request.get.GetFoodCommandSyntax;
import storage.syntax.http.request.get.GetFoodReportCommandSyntax;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class GetFoodReportCommand implements Command {

    private final String energy = "Energy";
    private final String protein = "Protein";
    private final String totalLipids = "Total lipid (fat)";
    private final String carbohydrates = "Carbohydrate, by difference";
    private final String fiber = "Fiber, total dietary";
    private final int fdcIdIndex = 1;
    private URI uri;
    private List<String> command;
    private DataExchanger exchanger;

    public GetFoodReportCommand(DataExchanger exchanger, List<String> commandInput) {
        this.exchanger = exchanger;
        this.command = commandInput;
    }
    @Override
    public void executeRequest() throws URISyntaxException, IOException, InterruptedException, MissingExtractedDataException {

        FoodByFdcId extractedFoodFromStorage = exchanger.retrieveData(Integer.parseInt(command.get(fdcIdIndex)));
        if (extractedFoodFromStorage != null) {
            clientOutput(extractedFoodFromStorage);
            return;
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
            clientOutput(foodInOrderNutrients);
        } catch (MissingExtractedDataException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private URI configureUri() throws URISyntaxException {
        return new URI(HttpHandler.SCHEME,
                HttpHandler.HOST_FOOD_API,
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
                .header(GetFoodReportCommandSyntax.API_KEY_NAME, GetFoodCommandSyntax.API_KEY_VALUE)
                .uri(uri)
                .GET()
                .build();
    }


    private FoodByFdcId getNewFoodByFdcId(FoodByFdcId food) {
        List<String> nutrients = List.of(energy, protein, totalLipids, carbohydrates, fiber); // contains the nutrients in the right order

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


    private void clientOutput(FoodByFdcId extractedFood) {
        System.out.println(extractedFood.description());
        System.out.println(extractedFood.ingredients());

        System.out.println("Nutrients:");
        System.out.println("Energy - " + extractedFood.foodNutrients().get(0).amount());
        System.out.println("Protein - " + extractedFood.foodNutrients().get(1).amount());
        System.out.println("Total lipid(fat) - " + extractedFood.foodNutrients().get(2).amount());
        System.out.println("Carbohydrates - " + extractedFood.foodNutrients().get(3).amount());
        System.out.println("Fiber - " + extractedFood.foodNutrients().get(4).amount());
    }
}
