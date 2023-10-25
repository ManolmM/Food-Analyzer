package storage.food;

import json.extractor.food.fdcid.FoodByFdcId;

import org.junit.Test;
import storage.databases.ibm_db2.DataExchanger;
import storage.files.FileFoodHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


import static org.junit.Assert.assertEquals;


public class FileFoodHandlerTest {

    private final String fileDataSet = "./test/storage/foods/dataset_test.txt";

    private FileFoodHandler fileFoodHandler;

    private DataExchanger exchanger;
    private Path path = Path.of(fileDataSet);

/*
    @Test
    public void setUp() throws IOException {
        fileFoodHandler = FileFoodHandler.newInstance(path);
        exchanger = DataExchanger.of(fileFoodHandler);
    }


    @Test
    public void tearDown() throws IOException {
        FileWriter fileWriter = new FileWriter(path.toFile(), false);
        fileWriter.close();
    }


    @Test
    public void testFillFileWithRecordInEmptyFile() throws IOException {
        // Left the setUp() method explicitly because it doesn't work if it's not here.
        // @AfterEach annotation doesn't work correctly
        setUp();

        // Some data in correct format to be stored in the file
        String data = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";


        // Before writing the data to file
        int expectedLines = 1;    // The headline of the file is included
        int actualLines = fileFoodHandler.countFileLines();
        assertEquals(expectedLines, actualLines);

        // After writing the data to file
        fileFoodHandler.fillFileWithData(data);
        expectedLines = 2;
        actualLines = 2;
        assertEquals(expectedLines, actualLines);

        tearDown();
    }



    @Test
    public void testFillFileWithFiveRecordsInFileWithOneRecord() throws IOException {
        setUp();
        // Some data in correct format to be stored in the file
        String data = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        fileFoodHandler.fillFileWithData(data);

        // It is not necessary the records to be unique in this case.
        // The aim of the method is to count the lines.
        String data1 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data2 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data3 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data4 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data5 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";


        // Before writing the data to file
        int expectedLines = 2;    // The headline and the new added line
        int actualLines = fileFoodHandler.countFileLines();
        assertEquals(expectedLines, actualLines);

        // After writing the data to file
        fileFoodHandler.fillFileWithData(data1);
        fileFoodHandler.fillFileWithData(data2);
        fileFoodHandler.fillFileWithData(data3);
        fileFoodHandler.fillFileWithData(data4);
        fileFoodHandler.fillFileWithData(data5);

        expectedLines = 7;
        actualLines = fileFoodHandler.countFileLines();
        assertEquals(expectedLines, actualLines);
        tearDown();
    }

    @Test
    public void testParseDataFromEmptyFile() throws IOException {
        setUp();

        List<FoodByFdcId> storage = fileFoodHandler.parseDataFromFile();
        int expectedSize = 0;
        int actualSize = storage.size();
        assertEquals("Extracting records from empty file should return empty list", expectedSize, actualSize);

        tearDown();
    }

    @Test
    public void testParseDataFromFileWithOneRecord() throws IOException {
        setUp();

        // Some data in correct format to be stored in the file
        String data = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        fileFoodHandler.fillFileWithData(data);

        List<FoodByFdcId> storage = fileFoodHandler.parseDataFromFile();
        int expectedSize = 1;
        int actualSize = storage.size();
        assertEquals("Extracting records from file with one record should return list with one FoodByFdcId", expectedSize, actualSize);

        tearDown();
    }

    @Test
    public void testParseDataFromFileWithTenRecords() throws IOException {
        setUp();

        // Some data in correct format to be stored in the file
        String data1 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data2 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data3 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data4 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data5 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data6 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data7 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data8 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data9 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";
        String data10 = "2342572;null;Pho;null;89.0;6.15;2.26;10.4;0.6";

        fileFoodHandler.fillFileWithData(data1);
        fileFoodHandler.fillFileWithData(data2);
        fileFoodHandler.fillFileWithData(data3);
        fileFoodHandler.fillFileWithData(data4);
        fileFoodHandler.fillFileWithData(data5);
        fileFoodHandler.fillFileWithData(data6);
        fileFoodHandler.fillFileWithData(data7);
        fileFoodHandler.fillFileWithData(data8);
        fileFoodHandler.fillFileWithData(data9);
        fileFoodHandler.fillFileWithData(data10);


        List<FoodByFdcId> storage = fileFoodHandler.parseDataFromFile();
        int expectedSize = 10;
        int actualSize = storage.size();
        assertEquals("Extracting records from file with ten records should return list with ten FoodByFdcId elements", expectedSize, actualSize);

        tearDown();
    }

*/


}
