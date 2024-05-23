package org.example;

import java.net.Socket;

/**
 * @author cevher
 */
public class Request {
    private final Socket socket;

    public Request(Socket socket) {
        this.socket = socket;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
