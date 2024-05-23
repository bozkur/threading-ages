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
public class FuturesFunctionalDecomposition {

    private final ServerSocket server;
    private final ExecutorService exec;

    public FuturesFunctionalDecomposition(int port) throws IOException {
        this.server = new ServerSocket(port);
        exec = Executors.newFixedThreadPool(16);
    }

    void handleRequest(Socket socket) {
        var request = new Request(socket);

        var futureWeather = CompletableFuture.supplyAsync(() -> Weather.fetch(request), exec);
        var futureRestaurants = CompletableFuture.supplyAsync(() -> Restaurants.fetch(request), exec);
        var futureTheaters = CompletableFuture.supplyAsync(() -> Theaters.fetch(request), exec);

        CompletableFuture.completedFuture(new Page(request))
                .thenCombine(futureWeather, Page::setWeather)
                .thenCombine(futureRestaurants, Page::setRestaurants)
                .thenCombine(futureTheaters, Page::setTheaters)
                .thenAccept(Page::send);
    }
}
