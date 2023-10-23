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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class InsertQueryTest {

    DB2Connection db2;

    @Mock
    private Nutrient nutrientMock = Mockito.mock(Nutrient.class);
    @InjectMocks
    private FoodNutrients foodNutrients;

    private FoodByFdcId foodByFdcId;

    private InsertQuery insertQuery;
    @Test
    @Before
    public void setUp() {
        db2 = new DB2Connection();
        db2.openConnection();
    }
/*
    @Test
    public void testExecuteWhenDataBaseIsEmpty() throws SQLException {
        setUp();


        foodNutrients = new FoodNutrients(nutrientMock, 10F);
        List<FoodNutrients> foodNutrientsList = List.of(foodNutrients, foodNutrients, foodNutrients, foodNutrients, foodNutrients);
        foodByFdcId = new FoodByFdcId(1234,null,
                null, "00000000", foodNutrientsList);
        insertQuery = new InsertQuery(foodByFdcId);

        ResultSet resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD");

        insertQuery.execute();     // Inserts a new record in the table.
        resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD");

        resultSet.next();     // Moves to the first row of the table.

        int expectedFdcId = 1234;
        int actualFdcId = Integer.parseInt(resultSet.getString(1)); // Gets the value of the current row = 1 and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        String expectedGtin = "00000000";
        String actualGtin = resultSet.getString(2); // Gets the value of the current row = 1 and column = 2.
        assertEquals(expectedGtin, actualGtin);

        //String expectedDesc = null;
        String actualDesc = resultSet.getString(3); // Gets the value of the current row = 1 and column = 2.
        assertEquals(null, actualDesc);

        //String expectedIng = null;
        String actualIng = resultSet.getString(4); // Gets the value of the current row = 1 and column = 2.
        assertEquals(null, actualIng);


    }

    @Test
    public void testInsertIntoVIEW_FOOD_ALONG_WITH_NUTRIENTSNonNullableValues() throws SQLException {


        foodNutrients = new FoodNutrients(nutrientMock, 10F);
        List<FoodNutrients> foodNutrientsList = List.of(foodNutrients, foodNutrients, foodNutrients, foodNutrients, foodNutrients);
        foodByFdcId = new FoodByFdcId(1000,"description",
                "Ingredients", "00000000", foodNutrientsList);
        insertQuery = new InsertQuery(foodByFdcId);

        ResultSet resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD");
        insertQuery.execute();      // Inserts a new record in the table.


        resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.BRANDED_FOOD");
        resultSet.last();

        int expectedFdcId = 1111;
        int actualFdcId = Integer.parseInt(resultSet.getString(1)); // Gets the value of the last row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        String expectedGtin = "00000000";
        String actualGtin = resultSet.getString(2); // Gets the value of the last row and column = 2.
        assertEquals(expectedGtin, actualGtin);

        String expectedDesc = "description";
        String actualDesc = resultSet.getString(3); // Gets the value of the last row and column = 3.
        assertEquals(expectedDesc, actualDesc);

        String expectedIng = "Ingredients";
        String actualIng = resultSet.getString(4); // Gets the value of the last row and column = 4.
        assertEquals(expectedIng, actualIng);


        resultSet = DB2Connection.statement.executeQuery("SELECT * FROM FN45798.NUTRIENTS");
        resultSet.last();

        expectedFdcId = 1000;
        actualFdcId = Integer.parseInt(resultSet.getString(1)); // Gets the value of the last row and column = 1.
        assertEquals(expectedFdcId, actualFdcId);

        float expectedEnergyAmount = 10;
        float actualEnergyAmount = Float.parseFloat(resultSet.getString(2)); // Gets the value of the current row = 1 and column = 2.
        assertEquals(expectedEnergyAmount, actualEnergyAmount);

        float expectedProteinAmount = 10;
        float actualProteinAmount = Float.parseFloat(resultSet.getString(3)); // Gets the value of the current row = 1 and column = 3.
        assertEquals(expectedProteinAmount, actualProteinAmount);

        float expectedFatAmount = 10;
        float actualFatAmount = Float.parseFloat(resultSet.getString(4)); // Gets the value of the current row = 1 and column = 4.
        assertEquals(expectedFatAmount, actualFatAmount);

        float expectedCarbAmount = 10;
        float actualCarbAmount = Float.parseFloat(resultSet.getString(5)); // Gets the value of the current row = 1 and column = 5.
        assertEquals(expectedCarbAmount, actualCarbAmount);

        float expectedFiberAmount = 10;
        float actualFiberAmount = Float.parseFloat(resultSet.getString(6)); // Gets the value of the current row = 1 and column = 6.
        assertEquals(expectedFiberAmount, actualFiberAmount);
    }

*/


}
