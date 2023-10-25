package storage.databases.ibm_db2;

import command.queries.InsertQuery;
import command.queries.SelectAll;
import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class DataExchanger {

    private List<FoodByFdcId> storage;
    private InsertQuery insertQuery;
    private SelectAll selectAll;

    // Inheritance is not allowed.
    private DataExchanger(InsertQuery insertQuery)throws SQLException {
        selectAll = new SelectAll();
        storage = selectAll.selectAllFromVIEW_FOOD_ALONG_WITH_NUTRIENTS();  // loads each record.
        this.insertQuery = insertQuery;

        if (storage == null) {
            storage = new ArrayList<>();
        }
    }
    public static DataExchanger of(InsertQuery insertQuery) throws IOException, SQLException {
        return new DataExchanger(insertQuery);
    }


    /**
     * Search the food by the given fdcId from the storage.
     * Aims to prevent sending a query to the database.
     *
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
    public void storeData(FoodByFdcId record) throws MissingExtractedDataException, IOException, SQLException {
        if (record == null) {
            throw new MissingExtractedDataException("Empty food by fdcId to insert into the database");
        }

        if (retrieveData(record.fdcId()) == null) {

            insertQuery.insertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTS(record); // Adds the record directly to the database.
            storage.add(record);          // Collects the instance of the record locally while running the program.
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