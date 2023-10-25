package storage.databases.ibm_db2;

import java.io.IOException;
import java.sql.*;

public class DB2Connection {

    private Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public void openConnection() {

// Step 1: Load IBM DB2 JDBC driver

        try {
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        }

        catch(Exception cnfex) {

            System.out.println("Problem in loading or registering IBM DB2 JDBC driver");
            cnfex.printStackTrace();
        }

// Step 2: Opening database connection


        try {

            connection = DriverManager.getConnection("jdbc:db2://62.44.108.24:50000/SAMPLE", "db2admin", "db2admin");

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        }

        catch(SQLException s){

            s.printStackTrace();
        }
    }

    public void closeConnection(){

        try {

            if(null != connection) {

                // cleanup resources, once after processing

                if (resultSet != null) {
                    resultSet.close();
                }

                statement.close();


                // and then finally close connection

                connection.close();

            }

        }

        catch (SQLException s) {

            s.printStackTrace();

        }

    }

    public Statement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public static void main(String[] args) throws IOException,SQLException {
        DB2Connection db2 = new DB2Connection();
        db2.openConnection();
        //ExtractCommand ex = new ExtractCommand(resultSet,statement);

        //ex.extract();
        db2.closeConnection();
    }

}
