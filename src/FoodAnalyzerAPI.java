import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import network.http.handler.HttpService;

import java.util.Scanner;

public class FoodAnalyzerAPI {

    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter query: ");
            String command = scanner.nextLine();
            Command newCommand = CommandCreator.newCommand(command);

            CommandExecutor executor = CommandExecutor.of();
            HttpService service = HttpService.newInstance();
            executor.takeCommand(newCommand);
            executor.placeCommand(service);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }
}
