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
public class FuturesCallbackServer {
    private final ServerSocket server;
    private final ExecutorService exec;

    public FuturesCallbackServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        exec = Executors.newFixedThreadPool(16);
    }

    public void run() throws IOException {
        while (!server.isClosed()) {
            var socket = server.accept();
            exec.execute(() -> handleRequest(socket));
        }
        exec.close();
    }

    void handleRequest(Socket socket) {
        var request = new Request(socket);

        var futureWeather = CompletableFuture.supplyAsync(() -> Weather.fetch(request), exec);
        var futureRestaurants = CompletableFuture.supplyAsync(() -> Restaurants.fetch(request), exec);
        var futureTheaters = CompletableFuture.supplyAsync(() -> Theaters.fetch(request), exec);

        var page = new Page(request);

        futureWeather.thenAccept(weather ->
                futureRestaurants.thenAccept(restaurants ->
                        futureTheaters.thenAccept(theaters ->
                                page.setWeather(weather)
                                        .setRestaurants(restaurants)
                                        .setTheaters(theaters)
                                        .send())));
    }
}

