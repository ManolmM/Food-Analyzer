package network.http;

import network.http.handler.HttpHandler;

import java.util.PriorityQueue;
import java.util.Queue;

public class HttpConnectionProvider {

    private final static int CAPACITY = 10;
    private Queue<HttpHandler> freeHttpHandler;

    private HttpConnectionProvider() {
        this.freeHttpHandler = new PriorityQueue<>(CAPACITY,
                (h1, h2) -> Boolean.compare(h2.isFree(), h1.isFree()));  // A free http handler is atop of the heap
    }

    public static HttpConnectionProvider of() {
        return new HttpConnectionProvider();
    }

    private HttpHandler getInstance() {
        if (freeHttpHandler.peek().isFree()) {
            freeHttpHandler.peek().setBusy();           // Always check whether the peek is busy(if all the handlers are busy).
            return freeHttpHandler.peek();
        }

        freeHttpHandler.add(HttpHandler.newInstance()); // Adds new http handler to the heap.
        freeHttpHandler.peek().setBusy();               // The new handler have to be atop of the heap. Sets it as occupied.
        return freeHttpHandler.peek();                  // Returns the newest instance.
    }


}
