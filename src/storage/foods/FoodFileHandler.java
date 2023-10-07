package storage.foods;

import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FoodFileHandler {
    private final String fileDataSet = "./src/storage/foods/dataset.csv";
    private final String headLine = "FdcId, Gtin, Description, Ingredients, " +
                                    "Energy, Protein, Total lipid (fat), Carbohydrate, Fiber" + System.lineSeparator();
    private Path filePath;
    private FoodFileHandler() throws IOException {
        try {
            setUpFile();
        } catch (InvalidPathException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public static FoodFileHandler newInstance() throws IOException {
        return new FoodFileHandler();
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

    public int countFileLines() throws IOException {
        try (var reader = new BufferedReader(new FileReader(fileDataSet))) {
            int lines = 0;
            while (reader.readLine() != null) {
                lines++;
            }
            return lines;

        } catch (IOException e) {
            throw new IOException("IO exception occurred while reading the file lines");
        }
    }

    public List<FoodByFdcId> parseDataFromFile() throws FileNotFoundException, IOException{
        try (var dataInput = new BufferedReader(new FileReader(fileDataSet))) {
            List<FoodByFdcId> storage = dataInput.lines().skip(1)
                    .map(line -> {
                        String[] fields = line.split(";");

                        int fdcId = Integer.parseInt(fields[0]);
                        String gtinUpc = fields[1];
                        String description = fields[2];
                        String ingredients = fields[3];

                        List<FoodNutrients> foodNutrients = new ArrayList<>();
                        int fieldsSize = fields.length;
                        for (int i = 4; i < fieldsSize - 1; i++) {
                            Nutrient nutrient = new Nutrient("Energy")
                        }
                    })
        }
            return storage;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
