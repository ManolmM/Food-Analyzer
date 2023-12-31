package command.queries;

import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import storage.databases.ibm_db2.DB2Connection;
import storage.syntax.database.DatabaseView;
import storage.syntax.http.request.get.GetFoodReportCommandSyntax;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class InsertQuery {


    /**
     * Transforms the record in a suitable form when it is inserted into VIEW_FOOD_ALONG_WITH_NUTRIENTS.
     * @throws IllegalArgumentException if the record is null or if a problem occurs while inserting the record.
     */
    public void insertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTS(FoodByFdcId record) throws SQLException {
        if (record == null) {
            throw new IllegalArgumentException("FoodByFdcId should not be null");
        }
        try {
            String viewQuery = fromFoodByFdcIdToQuery(record);
            DB2Connection.statement.execute(viewQuery);
        } catch (SQLSyntaxErrorException e) {
            throw new SQLSyntaxErrorException(("Unable to insert data into the database"));
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Transforms the FoodByFdcId to a query that is suitable for inserting into VIEW_FOOD_ALONG_WITH_NUTRIENTS.
     *
     */
    private String fromFoodByFdcIdToQuery(FoodByFdcId record) {
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO FN45798." + DatabaseView.VIEW_FOOD_ALONG_WITH_NUTRIENTS + " VALUES (");
        query.append((record.fdcId())).append(", ");

        if (record.gtinUpc() != null) {
            query.append("'").append((record.gtinUpc())).append("'").append(", ");
        } else {
            query.append("null").append(", ");
        }

        if (record.description() != null) {
            query.append("'").append((record.description())).append("'").append(", ");
        } else {
            query.append("null").append(", ");
        }

        if (record.ingredients() != null) {
            query.append("'").append((record.ingredients())).append("'").append(", ");
        } else {
            query.append("null").append(", ");
        }

        List<FoodNutrients> foodNutrientsList = record.foodNutrients();

        int nutrientListSize = GetFoodReportCommandSyntax.nutrientsOrderList.size() - 1;
        for (int i = 0; i < nutrientListSize; i++) {

            query.append(foodNutrientsList.get(i).amount()).append(", ");
        }

        query.append(foodNutrientsList.get(nutrientListSize - 1).amount());
        query.append(')');
        return query.toString();
    }
}
