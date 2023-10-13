package storage.syntax.http.request.get;

import storage.foods.nutrients.NutrientCollection;

import java.util.List;

public class GetFoodReportCommandSyntax {
    public static final String API_KEY_NAME = "X-Api-Key";
    public static final String API_KEY_VALUE = "sbzt765lR4REePn0OdrZ2XpVTrk3HVqZ4BPQe4iX";
    public static final String PREFIX_PATH_FOOD = "/fdc/v1/food/";
    public static final String ENERGY_NUMBER_PARAMETER = "nutrients=208";
    public static final String PROTEIN_NUMBER_PARAMETER = "nutrients=203";
    public static final String FAT_NUMBER_PARAMETER = "nutrients=204";
    public static final String FIBER_NUMBER_PARAMETER = "nutrients=291";
    public static final String CARBOHYDRATES_NUMBER_PARAMETER = "nutrients=205";

    // The desired order of the nutrients.
    public static List<String> nutrientsOrderList = List.of(NutrientCollection.ENERGY,
            NutrientCollection.PROTEIN,
            NutrientCollection.TOTAL_LIPIDS,
            NutrientCollection.CARBOHYDRATES,
            NutrientCollection.FIBER);

}
