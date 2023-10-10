import command.CommandExecutor;
import network.https.properties.Properties;
import network.tcp.Server;

import java.nio.file.Path;

public class FoodAnalyzerAPI {

    public static void main(String[] args) {

        Path path = Path.of("./src/storage/foods/dataset.txt");
        CommandExecutor executor = CommandExecutor.newInstance();
        Server server = new Server(path, Properties.SERVER_PORT, executor);
        server.start();

        //get-food-report 2032440
        //get-food-report 2110388
    }
}