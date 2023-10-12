package json.extractor.food.fdcid;

import com.google.gson.annotations.SerializedName;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;

import java.util.List;

public record FoodByFdcId(int fdcId,
                          String description,
                          String ingredients,
                          String gtinUpc,
                          List<FoodNutrients> foodNutrients) {
}
