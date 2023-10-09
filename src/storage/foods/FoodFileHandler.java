package storage.foods;

import json.extractor.food.fdcid.FoodByFdcId;
import json.extractor.food.nutrient.FoodNutrients;
import json.extractor.food.nutrient.Nutrient;
import storage.foods.nutrients.NutrientCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FoodFileHandler {
    //private final String fileDataSet = "./src/storage/foods/dataset.csv";
    private final String headLine = "FdcId, Gtin, Description, Ingredients, " +
                                    "Energy, Protein, Total lipid (fat), Carbohydrate, Fiber" + System.lineSeparator();
    private Path filePath;
    private FoodFileHandler(Path filePath) throws IOException {
        try {
            setUpFile(filePath);
        } catch (InvalidPathException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public static FoodFileHandler newInstance(Path filePath) throws IOException {
        return new FoodFileHandler(filePath);
    }

    private void setUpFile(Path fileDataSet) throws IOException {
        try {
            filePath = fileDataSet;
            if (countFileLines() == 0) {
                try (var fileWriter = Files.newBufferedWriter(filePath)) {
                    fileWriter.write(headLine);
                    fileWriter.flush();
                } catch (IOException e) {
                    throw new IOException("Unable to open dataset file.");
                }
            }
        } catch (IOException e) {
            throw e;
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

    protected int countFileLines() throws IOException {
        try (var reader = Files.newBufferedReader(filePath)) {
            int lines = 0;
            while (reader.readLine() != null) {
                lines++;
            }
            return lines;

        } catch (IOException e) {
            throw new IOException("IO exception occurred while reading the file lines");
        }
    }

    public List<FoodByFdcId> parseDataFromFile() throws FileNotFoundException, IOException {
        try (var dataInput = Files.newBufferedReader(filePath)) {
            List<String> nutrientList = List.of(NutrientCollection.ENERGY, NutrientCollection.PROTEIN,
                                             NutrientCollection.TOTAL_LIPIDS, NutrientCollection.CARBOHYDRATES,
                                             NutrientCollection.FIBER);
            int fdcIdIndex = 0;
            int gtinUpcIndex = 1;
            int descriptionIndex = 2;
            int ingredientsIndex = 3;

            List<FoodByFdcId> storage = dataInput.lines().skip(1)
                    .map(line -> {
                        String[] fields = line.split(";");

                        int fdcId = Integer.parseInt(fields[fdcIdIndex]);
                        String gtinUpc = fields[gtinUpcIndex];
                        String description = fields[descriptionIndex];
                        String ingredients = fields[ingredientsIndex];

                        List<FoodNutrients> foodNutrients = new ArrayList<>();
                        int nutrientListSize = nutrientList.size();
                        int offset = 4;
                        for (int i = 0; i < nutrientListSize; i++) {
                            String nutrientName = nutrientList.get(i);
                            double amount = Double.parseDouble(fields[offset + i]);
                            Nutrient newNutrient = new Nutrient(nutrientName);
                            foodNutrients.add(new FoodNutrients(newNutrient, amount));
                        }
                        return new FoodByFdcId(fdcId, description, ingredients, gtinUpc, foodNutrients);
                    }).toList();

            return storage;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
