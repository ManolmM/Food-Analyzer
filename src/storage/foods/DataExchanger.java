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
        storage = new ArrayList<>(foodFileHandler.parseDataFromFile());
        if (storage == null) {
            storage = new ArrayList<>();
        }
    }

    public static DataExchanger of(FoodFileHandler foodFileHandler) throws IOException {
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
//2110388;076186000035;JAPANESE STYLE NOODLES & BEEF FLAVORED-SOUP;ORIENTAL NOODLES: ENRICHED WHEAT FLOUR (WHEAT FLOUR, NIACIN, REDUCED IRON, THIAMINE MONONITRATE, RIBOFLAVIN, FOLIC ACID), PALM OIL (TBHQ AND CITRIC ACID ADDED TO PROTECT FLAVOR), TAPIOCA STARCH, SOY SAUCE (WATER, WHEAT, SOYBEANS, SALT, SODIUM BENZOATE [PRESERVATIVE]), SALT, GUAR GUM, SODIUM CARBONATE, TOCOPHEROLS, POTASSIUM CARBONATE. SOUP BASE: SALT, SOY SAUCE POWDER (SOY SAUCE[WHEAT, SOYBEANS, SALT], MALTODEXTRIN, SALT), MONOSODIUM GLUTAMATE, SUGAR, CARAMEL COLOR (CONTAINS SULFITES), GARLIC POWDER, CORN STARCH, MALTODEXTRIN, SPICES, ONION POWDER, LEEK CHIPS, CHICKEN BROTH POWDER (MALTODEXTRIN, CHICKEN BROTH FLAVOR[CONTAINS CHICKEN BROTH, SALT, FLAVOR]), BEEF STOCK POWDER, DISODIUM INOSINATE, DISODIUM GUANYLATE, YEAST EXTRACT, CORN OIL.;460.0;10.0;18.0;65.0;4.0
//2032440;04178900412;RAMEN NOODLE SOUP, BEEF;RAMEN NOODLE INGREDIENTS: ENRICHED WHEAT FLOUR (WHEAT FLOUR, NIACIN, REDUCED IRON, THIAMINE MONONITRATE, RIBOFLAVIN, FOLIC ACID), VEGETABLE OIL (CONTAINS ONE OR MORE OF THE FOLLOWING: CANOLA, COTTONSEED, PALM) PRESERVED BY TBHQ, CONTAINS LESS THAN 1% OF: SALT, SOY SAUCE (WATER, WHEAT, SOYBEANS, SALT), POTASSIUM CARBONATE, SODIUM (MONO, HEXAMETA, AND/OR TRIPOLY) PHOSPHATE, SODIUM CARBONATE, TURMERIC. SOUP BASE INGREDIENTS: MONOSODIUM GLUTAMATE, SUGAR, SEA SALT, CONTAINS LESS THAN 1% OF: DEHYDRATED SOY SAUCE (WHEAT, SOYBEANS, SALT), HYDROLYZED CORN, WHEAT AND SOY PROTEIN, DEHYDRATED VEGETABLES (ONION, GARLIC, CHIVE), MALTODEXTRIN, CARAMEL COLOR, POTASSIUM CHLORIDE, SPICES, BEEF FAT, YEAST EXTRACT, LACTOSE, NATURAL FLAVOR, DISODIUM INOSINATE, DISODIUM GUANYLATE.;442.0;11.63;16.28;60.47;2.3