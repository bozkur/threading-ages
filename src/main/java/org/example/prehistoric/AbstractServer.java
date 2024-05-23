package org.example.prehistoric;

import org.example.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author cevher
 */
public class AbstractServer {
    protected final ServerSocket server;

    public AbstractServer(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    protected void handleRequest(Socket socket) {
        var request = new Request(socket);              // parse a request
        var page = new Page(request);                   // create a base page
        page.setWeather(Weather.fetch(request))         // add weather info to the page
                .setRestaurants(Restaurants.fetch(request)) // add restaurant info to the page
                .setTheaters(Theaters.fetch(request))      // add theater info to the page
                .send();                                    // send the page back as a response
    }
}
