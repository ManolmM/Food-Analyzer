package command.queries;

import org.junit.Before;
import org.junit.Test;
import storage.databases.ibm_db2.DB2Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SelectAllTest {

    DB2Connection db2;

    @Test
    @Before
    public void setUp() {
        db2 = new DB2Connection();
        db2.openConnection();
    }

    @Test
    public void testSelectAllFromEmptyTEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS() throws SQLException {

        String query = "SELECT * FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS";
        assertDoesNotThrow(() -> DB2Connection.statement.executeQuery(query),
                "Selecting from empty view should not throw exception");

    }

    @Test
    public void testSelectAllFromNonEmptyTEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS() throws SQLException {
        String insertQuery1 = "INSERT INTO FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS " +
                "VALUES(1000, '00000000', null, null, 10.0, 10.0, 10.0, 10.0, 10.0)";

        String insertQuery2 = "INSERT INTO FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS " +
                "VALUES(1100, '00000000', null, null, 10.0, 10.0, 10.0, 10.0, 10.0)";
        String selectQuery1 = "SELECT * FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS";
        String selectQuery2 = "SELECT * FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS";   // No such record with that fdcId.


        assertDoesNotThrow(() -> DB2Connection.statement.execute(insertQuery1));
        assertDoesNotThrow(() -> DB2Connection.statement.execute(insertQuery2));


        assertDoesNotThrow(() -> DB2Connection.statement.executeQuery(selectQuery1),
                "Selecting all rows from non-empty view should not throw exception");
        //assertThrows(SQLException.class, () -> DB2Connection.statement.executeQuery(selectQuery2),
        //        "Selecting invalid(missing) field should throw exception");

        ResultSet response = DB2Connection.statement.executeQuery(selectQuery2);
        int counterRows = 1;
        int expectedRows = 2;
        response.first();
        while (response.next()) {
            counterRows++;
        }

        assertEquals(expectedRows, counterRows, "Inserting into empty view two records should have two records");

        String deleteQuery1 = "DELETE FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS" +
                " WHERE branded_food_fdcId = " + "1000";
        String deleteQuery2 = "DELETE FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS" +
                " WHERE branded_food_fdcId = " + "1100";
        assertDoesNotThrow(() -> DB2Connection.statement.execute(deleteQuery1));
        assertDoesNotThrow(() -> DB2Connection.statement.execute(deleteQuery2));
    }

    @Test
    public void ttestSelectAllFromEmptyTEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS() throws SQLException {

        String query = "SELECT * FROM FN45798.TEST_VIEW_FOOD_ALONG_WITH_NUTRIENTS";
        assertDoesNotThrow(() -> DB2Connection.statement.executeQuery(query),
                "Selecting from empty view should throw exception");

    }
}
