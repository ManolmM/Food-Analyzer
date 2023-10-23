package storage.syntax.http.request.get;

public class GetFoodCommandSyntax {
    public static final String API_KEY_NAME = "X-Api-Key";
    public static final String API_KEY_VALUE = "sbzt765lR4REePn0OdrZ2XpVTrk3HVqZ4BPQe4iX";
    public static final String FOODS_PATH = "/fdc/v1/foods/search";
    public static final String PAGE_NUMBER = "pageNumber=";
    public static final String REQUIRED_API_PARAMETER_QUERY = "query=";
    public static final String REQUIRED_ALL_WORDS_PARAMETER = "requireAllWords=true&";
    public static final String DATA_TYPE = "dataType=&";   // extracted each category of food.
    public static final String DATA_TYPE_BRANDED = "dataType=Branded&"; // extracts only branded food.
    public static final String DATA_TYPE_FOUNDATION = "dataType=Foundation&"; // extracts only foundational food.
    public static final String DATA_TYPE_SURVEY = "dataType=Survey&"; // extracts only survey food.

}
