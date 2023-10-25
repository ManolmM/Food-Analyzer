package command.queries;

import org.junit.Before;
import org.junit.Test;
import storage.databases.ibm_db2.DB2Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import static org.junit.jupiter.api.Assertions.*;

public class SelectByFieldTest {

    private final int fdcIdIndex = 1;
    private final int gtinIndex = 2;
    private final int descriptionIndex = 3;
    private final int ingredientIndex = 4;
    private final int energyIndex = 2;
    private final int proteinIndex = 3;
    private final int totalLipidsIndex = 4;
    private final int carbIndex = 5;
    private final int fiberIndex = 6;

    @Test
    @Before
    public void setUp() {
        DB2Connection db2 = new DB2Connection();
        db2.openConnection();
    }

    @Test
    public void testSelectFromTEST_VIEW_FOOD_ALONG_WITH_NUTRIENTSByNonExistingFdcId() throws SQLException {

        int fdcId = 0;    // non-existing value;
        String query = "SELECT FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS WHERE branded_food_fdcId = " + fdcId;
        assertThrows(SQLSyntaxErrorException.class, () -> DB2Connection.statement.executeQuery(query),
                "Selecting non-existent field should throw exception");

    }

    @Test
    public void testSelectFromViewByExistingFdcId() throws SQLException {

        int fdcId = 1000;    // A real field from the view.
        String gtin = "00000000";

        String selectQuery = "SELECT * FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS WHERE branded_food_fdcId = " + fdcId;
        String insertQuery = "INSERT INTO FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS VALUES(1000, '00000000', null, null, 10.0, 10.0, 10.0, 10.0, 10.0)";
        assertDoesNotThrow(() -> DB2Connection.statement.execute(insertQuery));
        ResultSet response = DB2Connection.statement.executeQuery(selectQuery);

        response.first();   // Points to the record.
        assertEquals(fdcId, Integer.parseInt(response.getString(fdcIdIndex)));
        assertEquals(gtin, response.getString(gtinIndex));
        assertEquals(null, response.getString(descriptionIndex));
        assertEquals(null, response.getString(ingredientIndex));
        assertEquals(10.0, Float.parseFloat(response.getString(energyIndex)));
        assertEquals(10.0, Float.parseFloat(response.getString(proteinIndex)));
        assertEquals(10.0, Float.parseFloat(response.getString(totalLipidsIndex)));
        assertEquals(10.0, Float.parseFloat(response.getString(carbIndex)));
        assertEquals(10.0, Float.parseFloat(response.getString(fiberIndex)));

        String deleteQuery = "DELETE FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS" +
                " WHERE branded_food_fdcId = " + fdcId;
        assertDoesNotThrow(() -> DB2Connection.statement.execute(deleteQuery));

    }







}
