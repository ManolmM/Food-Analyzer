package command.queries;

import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import storage.databases.ibm_db2.DB2Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InsertQueryTest {

    DB2Connection db2;
    @Mock
    private Nutrient nutrientMock = Mockito.mock(Nutrient.class);
    @InjectMocks
    private FoodNutrients foodNutrients;

    private FoodByFdcId foodByFdcId;
    private final int fdcIdIndex = 1;
    private final int gtinIndex = 2;
    private final int descriptionIndex = 3;
    private final int ingredientIndex = 4;
    private final int energyIndex = 2;
    private final int proteinIndex = 3;
    private final int totalLipidsIndex = 4;
    private final int carbIndex = 5;
    private final int fiberIndex = 6;


    private InsertQuery insertQuery = new InsertQuery();;
    @Test
    @Before
    public void setUpConnection() {
        db2 = new DB2Connection();
        db2.openConnection();
    }

    @Test
    public void testInsertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTSNonNullableValues() throws SQLException {
        setUpConnection();

        foodNutrients = new FoodNutrients(nutrientMock, 10F);
        List<FoodNutrients> foodNutrientsList = List.of(foodNutrients, foodNutrients, foodNutrients, foodNutrients, foodNutrients);
        foodByFdcId = new FoodByFdcId(1000,"description",
                "Ingredients", "00000000", foodNutrientsList);

        insertQuery.insertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTS(foodByFdcId);      // Inserts a new record in the view.

        ResultSet resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD WHERE food_fdcId = 1000");
        resultSet.first();          // moves to the first row selected
        int expectedFdcId = 1000;
        int actualFdcId = Integer.parseInt(resultSet.getString(fdcIdIndex)); // Gets the value of the only row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        String expectedGtin = "00000000";
        String actualGtin = resultSet.getString(gtinIndex); // Gets the value of the only row and column = 2.
        assertEquals(expectedGtin, actualGtin);

        String expectedDesc = "description";
        String actualDesc = resultSet.getString(descriptionIndex); // Gets the value of the only row and column = 3.
        assertEquals(expectedDesc, actualDesc);

        String expectedIng = "Ingredients";
        String actualIng = resultSet.getString(ingredientIndex); // Gets the value of the only row and column = 4.
        assertEquals(expectedIng, actualIng);


        resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.NUTRIENTS WHERE food_fdcId = 1000");
        resultSet.first();          // moves to the first row selected


        actualFdcId = Integer.parseInt(resultSet.getString(fdcIdIndex)); // Gets the value of the only row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        float expectedEnergyAmount = 10;
        float actualEnergyAmount = Float.parseFloat(resultSet.getString(energyIndex)); // Gets the value of the only row and column = 2.
        assertEquals(expectedEnergyAmount, actualEnergyAmount);

        float expectedProteinAmount = 10;
        float actualProteinAmount = Float.parseFloat(resultSet.getString(proteinIndex)); // Gets the value of the only row and column = 3.
        assertEquals(expectedProteinAmount, actualProteinAmount);

        float expectedFatAmount = 10;
        float actualFatAmount = Float.parseFloat(resultSet.getString(totalLipidsIndex)); // Gets the value of the only row and column = 4.
        assertEquals(expectedFatAmount, actualFatAmount);

        float expectedCarbAmount = 10;
        float actualCarbAmount = Float.parseFloat(resultSet.getString(carbIndex)); // Gets the value of the only row and column = 5.
        assertEquals(expectedCarbAmount, actualCarbAmount);

        float expectedFiberAmount = 10;
        float actualFiberAmount = Float.parseFloat(resultSet.getString(fiberIndex)); // Gets the value of the only row and column = 6.
        assertEquals(expectedFiberAmount, actualFiberAmount);

        DB2Connection.statement.execute("DELETE FROM FN45798.VIEW_FOOD_ALONG_WITH_NUTRIENTS " +
                "WHERE BRANDED_FOOD_FDCID = 1000");
    }

    @Test
    public void testInsertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTSNullableValues() throws SQLException {
        setUpConnection();

        foodNutrients = new FoodNutrients(nutrientMock, 0.0F);
        List<FoodNutrients> foodNutrientsList = List.of(foodNutrients, foodNutrients, foodNutrients, foodNutrients, foodNutrients);
        foodByFdcId = new FoodByFdcId(1000,null,
                null, null, foodNutrientsList);

        insertQuery.insertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTS(foodByFdcId);   // Inserts a new record in the view.

        ResultSet resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD WHERE food_fdcId = 1000");
        resultSet.first();          // moves to the first row selected
        int expectedFdcId = 1000;
        int actualFdcId = Integer.parseInt(resultSet.getString(fdcIdIndex)); // Gets the value of the only row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);


        assertEquals(null, resultSet.getString(gtinIndex));
        assertEquals(null, resultSet.getString(descriptionIndex));
        assertEquals(null, resultSet.getString(ingredientIndex));

        resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.NUTRIENTS WHERE food_fdcId = 1000");
        resultSet.first();          // moves to the first row selected


        actualFdcId = Integer.parseInt(resultSet.getString(fdcIdIndex)); // Gets the value of the only row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        float expectedEnergyAmount = 0;
        float actualEnergyAmount = Float.parseFloat(resultSet.getString(energyIndex)); // Gets the value of the only row and column = 2.
        assertEquals(expectedEnergyAmount, actualEnergyAmount);

        float expectedProteinAmount = 0;
        float actualProteinAmount = Float.parseFloat(resultSet.getString(proteinIndex)); // Gets the value of the only row and column = 3.
        assertEquals(expectedProteinAmount, actualProteinAmount);

        float expectedFatAmount = 0;
        float actualFatAmount = Float.parseFloat(resultSet.getString(totalLipidsIndex)); // Gets the value of the only row and column = 4.
        assertEquals(expectedFatAmount, actualFatAmount);

        float expectedCarbAmount = 0;
        float actualCarbAmount = Float.parseFloat(resultSet.getString(carbIndex)); // Gets the value of the only row and column = 5.
        assertEquals(expectedCarbAmount, actualCarbAmount);

        float expectedFiberAmount = 0;
        float actualFiberAmount = Float.parseFloat(resultSet.getString(fiberIndex)); // Gets the value of the only row and column = 6.
        assertEquals(expectedFiberAmount, actualFiberAmount);

        DB2Connection.statement.execute("DELETE FROM FN45798.VIEW_FOOD_ALONG_WITH_NUTRIENTS " +
                "WHERE BRANDED_FOOD_FDCID = 1000");
    }


}
