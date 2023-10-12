package storage.foods;

/*
import exceptions.MissingExtractedDataException;
import json.extractor.food.fdcid.FoodByFdcId;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataExchangerTest {

    private final String fileDataSet = "./test/storage/foods/dataset_test.txt";

    @Mock
    private FoodByFdcId foodByFdcIdMock;
    @Mock
    private FileFoodHandler fileFoodHandlerMock;
    @InjectMocks
    private DataExchanger dataExchanger;


    // The only way Mock and InjectedMocks act correctly.
    // Test cases don't function properly without this method.
    @Test
    public void setUpMocks() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testRetrieveDataWhenStorageIsEmpty() throws IOException {
        setUpMocks();

        int fdcIdToRetrieve = 12345;

        List<FoodByFdcId> emptyStorage = new ArrayList<>();
        when(fileFoodHandlerMock.parseDataFromFile()).thenReturn(emptyStorage);

        FoodByFdcId result = dataExchanger.retrieveData(fdcIdToRetrieve);

        assertNull(result);
    }

    @Test
    public void testRetrieveDataWhenStorageIsNotEmpty() throws IOException, MissingExtractedDataException {
        setUpMocks();

    }


    //get-food-report 2032440
    //get-food-report 2110388
}
*/
