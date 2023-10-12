import command.CommandExecutor;
import network.https.properties.Properties;
import network.tcp.Server;

import java.nio.file.Path;

public class FoodAnalyzer {

    public static void main(String[] args) {

        Path path = Path.of("./src/storage/foods/dataset.txt");
        CommandExecutor executor = CommandExecutor.newInstance();
        Server server = Server.of(path, Properties.SERVER_PORT, executor);
        server.start();

    }
}