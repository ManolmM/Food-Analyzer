package storage.foods;

import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.name.FoodByName;
import json.extractor.food.nutrient.FoodNutrients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataExchanger {

    private List<FoodByFdcId> storage;
    private FileHandler fileHandler;

    private DataExchanger(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        storage = new ArrayList<>();
    }

    public static DataExchanger of(FileHandler fileHandler) {
        return new DataExchanger(fileHandler);
    }
    public FoodByFdcId retrieveData(int fdcId) {
        if (storage.isEmpty()) {
            return null;
        }

        List<FoodByFdcId> result = storage.stream()
                .filter(p -> p.fdcId() == fdcId)
                .toList();

        return result.get(0);
    }

    public void storeData(FoodByFdcId record) throws MissingExtractedDataException, IOException {
        if (record == null) {
            throw new MissingExtractedDataException("Empty food by fdcId to write in file");
        }
        String modifiedRecord = modifyData(record);
        fileHandler.fillFileWithData(modifiedRecord);
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
