import command.CommandExecutor;
import network.https.properties.Properties;
import network.tcp.Server;
import storage.databases.ibm_db2.DB2Connection;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class FoodAnalyzer {

    public static void main(String[] args) throws SQLException, IOException {

        DB2Connection db2 = new DB2Connection();
        db2.openConnection();

        CommandExecutor executor = CommandExecutor.newInstance();
        Server server = Server.of(Properties.SERVER_PORT, executor);
        server.start();

    }
}