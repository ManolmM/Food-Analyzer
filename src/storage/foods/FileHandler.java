package storage.foods;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileHandler {
    private final String fileDataSet = "./src/storage/foods/dataset.csv";
    private final String headLine = "FdcId, Gtin, Description, Ingredients, " +
                                    "Energy, Protein, Total lipid (fat), Carbohydrate, Fiber" + System.lineSeparator();
    private Path filePath;
    private FileHandler() throws IOException {
        try {
            setUpFile();
        } catch (InvalidPathException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public static FileHandler newInstance() throws IOException {
        return new FileHandler();
    }

    private void setUpFile() throws IOException {
        filePath = Path.of(fileDataSet);
        try (var fileWriter = Files.newBufferedWriter(filePath)) {
            fileWriter.write(headLine);
            fileWriter.flush();
        } catch (IOException e) {
            throw new IOException("Unable to open dataset file.");
        }
    }

    public void fillFileWithData(String data) throws IOException {
        try (var fileWrite = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            fileWrite.write(data);
            fileWrite.newLine();
            fileWrite.flush();
        } catch (IOException e) {
            throw new IOException("Unable to write new data to the dataset file.");
        }
    }

    public boolean checkExistence() {
        throw new UnsupportedOperationException("");
    }

}
