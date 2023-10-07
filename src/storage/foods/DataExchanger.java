package storage.foods;

import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;

import java.io.IOException;
import java.util.*;

public class DataExchanger {

    private List<FoodByFdcId> storage;
    private FoodFileHandler foodFileHandler;

    private DataExchanger(FoodFileHandler foodFileHandler) throws IOException {
        this.foodFileHandler = foodFileHandler;

        storage = new ArrayList<>();
    }

    public static DataExchanger of(FoodFileHandler foodFileHandler) {
        return new DataExchanger(foodFileHandler);
    }
    public FoodByFdcId retrieveData(int fdcId) {
        if (storage.isEmpty()) {
            return null;
        }

/*        List<FoodByFdcId> result = storage.stream()
                .filter(p -> p.fdcId() == fdcId)
                .toList();*/

        for (FoodByFdcId food : storage) {
            if (food.fdcId() == fdcId) {
                return food;
            }
        }

        return null;
    }

    public void storeData(FoodByFdcId record) throws MissingExtractedDataException, IOException {
        if (record == null) {
            throw new MissingExtractedDataException("Empty food by fdcId to write in file");
        }
        String modifiedRecord = modifyData(record);
        foodFileHandler.fillFileWithData(modifiedRecord);
        storage.add(record);
    }

    private String modifyData(FoodByFdcId record) {
        StringBuilder transformedData = new StringBuilder();

        transformedData.append((record.fdcId())).append(";")
                        .append(record.gtinUpc()).append(";")
                        .append(record.description()).append(";")
                        .append(record.ingredients()).append(";");

        int nutrientSize = record.foodNutrients().size();
        for (int i = 0; i < nutrientSize - 1; i++) {
            FoodNutrients nutrient = record.foodNutrients().get(i);
            transformedData.append(nutrient.amount()).append(";");
        }
        transformedData.append(record.foodNutrients().get(nutrientSize - 1).amount());

        return transformedData.toString();
    }
}
