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

public class SelectByField {

    private static ResultSet serverResponse;


    /**
     * Selects by fdcId from VIEW_FOOD_ALONG_WITH_NUTRIENTS.
     *
     * @return FoodByFdcId if available, null if not existing.
     **/
    public FoodByFdcId selectByFdcId(int fdcId) throws SQLException {
        String query = "SELECT branded_food_fdcId FROM FN45798." +
                DatabaseView.VIEW_FOOD_ALONG_WITH_NUTRIENTS +
                "WHERE branded_food_fdcId = " + fdcId;
        try {
            serverResponse = DB2Connection.statement.executeQuery(query);

            int columnSize = serverResponse.getMetaData().getColumnCount();
            int nutrientAmountIndex = 4;                            // Points the index where nutrients starts in foodElements.
            String[] foodElements = new String[columnSize];
            List<FoodNutrients> nutrientsList = new ArrayList<>();

            serverResponse.first();

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
            return newFood;

        } catch (SqlSyntaxErrorException e) {
            throw new SQLSyntaxErrorException("Invalid or not existing fdcId " + fdcId);
        } catch (SQLException e ) {
            throw e;
        }

    }

}
