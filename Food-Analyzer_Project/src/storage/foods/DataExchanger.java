package storage.foods;

import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;

import java.io.IOException;
import java.util.*;

public class DataExchanger {

    private List<FoodByFdcId> storage;
    private FileFoodHandler fileFoodHandler;

    // Inheritance is not allowed.
    private DataExchanger(FileFoodHandler fileFoodHandler) throws IOException {
        this.fileFoodHandler = fileFoodHandler;
        storage = new ArrayList<>(fileFoodHandler.parseDataFromFile());
        if (storage == null) {
            storage = new ArrayList<>();
        }
    }

    public static DataExchanger of(FileFoodHandler fileFoodHandler) throws IOException {
        return new DataExchanger(fileFoodHandler);
    }

    /**
     * Search the food by the given fdcId from the storage.
     * @return the FoodByFdcId from the storage.
     */
    public FoodByFdcId retrieveData(int fdcId) {
        if (storage.isEmpty()) {
            return null;
        }

        for (FoodByFdcId food : storage) {
            if (food.fdcId() == fdcId) {
                return food;
            }
        }

        return null;
    }

    /**
     * Stores a record of food.
     * @throws MissingExtractedDataException if null is passed to be stored.
     */
    public void storeData(FoodByFdcId record) throws MissingExtractedDataException, IOException {
        if (record == null) {
            throw new MissingExtractedDataException("Empty food by fdcId to write in file");
        }

        if (retrieveData(record.fdcId()) == null) {
            String modifiedRecord = modifyData(record);
            fileFoodHandler.fillFileWithData(modifiedRecord);
            storage.add(record);
        }
    }


    /**
     * Modify the record of food in the right format to be written in the file.
     * @return String version of the food record to be written in file.
     * @throws MissingExtractedDataException if null is passed to be stored.
     */
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