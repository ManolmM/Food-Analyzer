package network.http.handler;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

public class HttpHandler {

    public static final String SCHEME = "https";
    public static final String HOST_FOOD_API = "api.nal.usda.gov";
    public static final String HOST_BARCODE_API = "zxing.org";
    private boolean isFree = true;
    private HttpClient client;

    private URI uri;

    private HttpHandler() {
        client = HttpClient.newBuilder()
                //.connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    public static HttpHandler newInstance() {
        return new HttpHandler();
    }
    public HttpClient getClient() {
        return client;
    }
    public void setBusy() {
        isFree = false;
    }
    public void setFree() {
        isFree = true;
    }
    public boolean isFree() {
        return isFree;
    }

    public URI getUri() {
        return uri;
    }
}
