import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import network.http.handler.HttpHandler;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class FoodAnalyzerAPI {

    public static void main(String[] args) {


/*        try {
            Path filePath = Path.of("C:\\Users\\root\\Pictures\\Barcode\\upc-barcode.jpg");
            System.out.println("Success");
        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
        }*/
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter query: ");
            String command = scanner.nextLine();
            Command newCommand = CommandCreator.newCommand(command);

            CommandExecutor executor = CommandExecutor.newInstance();
            //HttpHandler service = HttpHandler.newInstance();
            executor.takeCommand(newCommand);
            executor.placeCommand();

            //get-food-report 2632390
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }


}
