package org.example.middleages;

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
public class FuturesDoubleThreadPoolServer {
    private final ServerSocket server;
    private final ExecutorService exec1;
    private final ExecutorService exec2;

    public FuturesDoubleThreadPoolServer(int port) throws IOException {
        server = new ServerSocket(port);
        exec1 = Executors.newFixedThreadPool(4);
        exec2 = Executors.newFixedThreadPool(12);
    }

    public void run() throws IOException {
        while (!server.isClosed()) {
            var socket = server.accept();
            exec1.execute(() -> handleRequest(socket));
        }
        exec1.close();
        exec2.close();
    }

    void handleRequest(Socket socket) {
        var request = new Request(socket);
        var futureWeather = CompletableFuture.supplyAsync(() -> Weather.fetch(request), exec2);
        var futureRestaurants = CompletableFuture.supplyAsync(() -> Restaurants.fetch(request), exec2);
        var futureTheaters = CompletableFuture.supplyAsync(() -> Theaters.fetch(request), exec2);

        new Page(request)
                .setWeather(futureWeather.join())
                .setRestaurants(futureRestaurants.join())
                .setTheaters(futureTheaters.join())
                .send();
    }
}
