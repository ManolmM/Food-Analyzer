package command.queries;

import com.ibm.db2.jcc.am.SqlSyntaxErrorException;
import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import storage.databases.ibm_db2.DB2Connection;
import storage.syntax.database.DatabaseView;
import storage.syntax.http.request.get.GetFoodReportCommandSyntax;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

public class SelectAll {
    private static ResultSet serverResponse;


    /**
     * Selects each row from VIEW_FOOD_ALONG_WITH_NUTRIENTS.
     *
     * @return FoodByFdcId list with the extracted data.
     **/
    public List<FoodByFdcId> selectAllFromVIEW_FOOD_ALONG_WITH_NUTRIENTS() throws SQLException {

        String query = "SELECT * FROM FN45798." + DatabaseView.VIEW_FOOD_ALONG_WITH_NUTRIENTS;
        try {
            serverResponse = DB2Connection.statement.executeQuery(query);


            int columnSize = serverResponse.getMetaData().getColumnCount();
            int nutrientAmountIndex = 4;                            // Points the index where nutrients starts in foodElements.
            String[] foodElements = new String[columnSize];
            List<FoodByFdcId> foodByFdcIdList = new ArrayList<>();
            List<FoodNutrients> nutrientsList = new ArrayList<>();

            while (serverResponse.next()) {

                // Stores each field from a row in a String array.
                for (int i = 1; i <= columnSize; i++) {
                    foodElements[i - 1] = serverResponse.getString(i);
                }


                // Fills nutrientsList with the stored values for each nutrient.
                List<String> orderedList = GetFoodReportCommandSyntax.nutrientsOrderList;
                int nutrientsSize = orderedList.size();
                for (int i = 0; i < nutrientsSize; i++) {
                    nutrientsList.add(new FoodNutrients(new Nutrient(orderedList.get(i)),
                            Float.parseFloat(foodElements[nutrientAmountIndex++])));
                }


                // Makes an instance of type FoodByFdcId that will be added to the list.
                FoodByFdcId newFood = new FoodByFdcId(Integer.parseInt(foodElements[0]), foodElements[3], foodElements[2], foodElements[1], nutrientsList);
                foodByFdcIdList.add(newFood);

                nutrientAmountIndex = 4;    // Sets the initial value.
            }

            return foodByFdcIdList;
        } catch (SqlSyntaxErrorException e) {
            throw new SQLSyntaxErrorException("Invalid query to VIEW_FOOD_ALONG_WITH_NUTRIENTS");
        } catch (SQLException e) {
            throw e;
        }
    }

}