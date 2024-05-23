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
public class OneThreadpoolServer {
    // DON'T DO THIS!
    private final ServerSocket server;
    private final ExecutorService exec;

    public OneThreadpoolServer(int port) throws IOException {
        server = new ServerSocket(port);
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
        var page = new Page(request);
        var done = new CountDownLatch(3);

        exec.execute(() -> {
            page.setWeather(Weather.fetch(request));
            done.countDown();
        });

        exec.execute(() -> {
            page.setRestaurants(Restaurants.fetch(request));
            done.countDown();
        });

        exec.execute(() -> {
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
