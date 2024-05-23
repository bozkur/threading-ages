package org.example.ancient;

import org.example.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cevher
 */
public class DoubleThreadPoolServer {
    private final ServerSocket server;
    private final ExecutorService exec1;
    private final ExecutorService exec2;

    public DoubleThreadPoolServer(int port) throws IOException {
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
        var page = new Page(request);
        var done = new CountDownLatch(3);

        exec2.execute(() -> {
            page.setWeather(Weather.fetch(request));
            done.countDown();
        });

        exec2.execute(() -> {
            page.setRestaurants(Restaurants.fetch(request));
            done.countDown();
        });

        exec2.execute(() -> {
            page.setTheaters(Theaters.fetch(request));
            done.countDown();
        });

        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        page.send();
    }
}
