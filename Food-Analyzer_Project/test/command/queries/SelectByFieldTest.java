package command.queries;

import org.junit.Before;
import org.junit.Test;
import storage.databases.ibm_db2.DB2Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import static org.junit.jupiter.api.Assertions.*;

public class SelectByFieldTest {

    DB2Connection db2;

    @Test
    @Before
    public void setUp() {
        db2 = new DB2Connection();
        db2.openConnection();
    }

    @Test
    public void testSelectFromViewByNonExistingFdcId() throws SQLException {

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
        assertDoesNotThrow(() -> DB2Connection.statement.executeUpdate(insertQuery));
        ResultSet response = DB2Connection.statement.executeQuery(selectQuery);

        response.first();   // Points to the record.
        assertEquals(fdcId, Integer.parseInt(response.getString(1)));
        assertEquals(gtin, response.getString(2));
        assertEquals(null, response.getString(3));
        assertEquals(null, response.getString(4));
        assertEquals(10.0, Float.parseFloat(response.getString(5)));
        assertEquals(10.0, Float.parseFloat(response.getString(6)));
        assertEquals(10.0, Float.parseFloat(response.getString(7)));
        assertEquals(10.0, Float.parseFloat(response.getString(8)));
        assertEquals(10.0, Float.parseFloat(response.getString(9)));

        String deleteQuery = "DELETE FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS" +
                " WHERE branded_food_fdcId = " + fdcId;
        assertDoesNotThrow(() -> DB2Connection.statement.execute(deleteQuery));

    }





}
