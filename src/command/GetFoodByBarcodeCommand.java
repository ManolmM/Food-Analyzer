package command;

import network.http.handler.HttpHandler;
import storage.foods.DataExchanger;
import storage.syntax.http.request.get.GetFoodCommandBarcodeSyntax;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;



public class GetFoodByBarcodeCommand implements Command {

    private URI uri;
    private List<String> command;
    private DataExchanger exchanger;
    public GetFoodByBarcodeCommand(DataExchanger exchanger, List<String> commandInput) {
        this.exchanger = exchanger;
        this.command = commandInput;
    }

    @Override
    public String executeRequest() throws URISyntaxException {
        throw new UnsupportedOperationException("");
/*        HttpRequest request;
        HttpClient client = HttpClient.newBuilder().build();

        try {
            Path filePath = Path.of(command.get(1));

            uri = configureUri();
            request = configureRequest(uri, filePath);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
            //containsBarcode(command.get(0));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            // invoke again commandInterface function/class
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        //get-food-by-barcode C:\\Users\\root\\Pictures\\Barcode\\upc-barcode.gif

    }

    private URI configureUri() throws URISyntaxException {
        return new URI("http",
                HttpHandler.HOST_BARCODE_API,
                GetFoodCommandBarcodeSyntax.PATH,
                null);
    }

    private HttpRequest configureRequest(URI uri, Path filePath) throws FileNotFoundException {
        return HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofFile(filePath))
                .header("Content-Disposition", "form-data; name=\"f\"; filename=\"" + filePath.getFileName() + "\"")
                .build();
    }



}