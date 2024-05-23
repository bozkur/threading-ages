package org.example.prehistoric;

import org.example.*;

import java.io.IOException;
import java.net.Socket;

/**
 * @author cevher
 */
public class ParallelDataFetchingThread extends AbstractServer{
    public ParallelDataFetchingThread(int port) throws IOException {
        super(port);
    }

    public void handleRequest4n(Socket socket) throws InterruptedException {
        var request = new Request(socket);
        var page = new Page(request);

        Thread t1 = new Thread(() -> page.setWeather(Weather.fetch(request)));
        Thread t2 = new Thread(() -> page.setRestaurants(Restaurants.fetch(request)));
        Thread t3 = new Thread(() -> page.setTheaters(Theaters.fetch(request)));
        t1.start(); t2.start(); t3.start();

        t1.join(); t2.join(); t3.join();
        page.send();
    }

    void handleRequest3n(Socket socket) throws InterruptedException {
        var request = new Request(socket);
        var page = new Page(request);

        Thread t1 = new Thread(() -> page.setWeather(Weather.fetch(request)));
        Thread t2 = new Thread(() -> page.setRestaurants(Restaurants.fetch(request)));
        t1.start(); t2.start();

        page.setTheaters(Theaters.fetch(request));//Use own thread for one job

        t1.join(); t2.join();
        page.send();
    }
}
