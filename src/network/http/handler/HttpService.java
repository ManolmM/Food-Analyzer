package network.http.handler;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HttpService {

    public static final String SCHEME = "https";
    public static final String HOST = "api.nal.usda.gov";
    public static final String PREFIX_PATH = "/fdc/v1/foods/search";
    public static final String PAGE_NUMBER = "pageNumber=";
    public static final String API_KEY_NAME = "X-Api-Key";
    public static final String API_KEY_VALUE = "sbzt765lR4REePn0OdrZ2XpVTrk3HVqZ4BPQe4iX";
    private boolean isFree = true;
    private HttpClient client;

    private HttpService() {
        client = HttpClient.newBuilder().build();
    }
    public static HttpService newInstance() {
        return new HttpService();
    }
    public HttpClient getClient() {
        return client;
    }
    public void setBusy() {
        isFree = false;
    }
    public boolean isFree() {
        return isFree;
    }

}
