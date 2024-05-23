package org.example.renaissance;

import org.example.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cevher
 */
public class FullAsyncDataFetching {

    private final ServerSocket server;
    private final ExecutorService exec;

    public FullAsyncDataFetching(int port) throws IOException {
        this.server = new ServerSocket(port);
        exec = Executors.newFixedThreadPool(16);
    }

    public void run() throws IOException {
        while (!server.isClosed()) {
            var socket = server.accept();
            handleRequest(socket);
        }
        exec.close();
    }

    void handleRequest(Socket socket) {
        var futureRequest = CompletableFuture.supplyAsync(() -> new Request(socket), exec);

        var futureWeather = futureRequest.thenApplyAsync(Weather::fetch, exec);
        var futureRestaurants = futureRequest.thenApplyAsync(Restaurants::fetch, exec);
        var futureTheaters = futureRequest.thenApplyAsync(Theaters::fetch, exec);

        futureRequest
                .thenApplyAsync(Page::new, exec)
                .thenCombine(futureWeather, Page::setWeather)
                .thenCombine(futureRestaurants, Page::setRestaurants)
                .thenCombine(futureTheaters, Page::setTheaters)
                .thenAccept(Page::send);
    }
}

