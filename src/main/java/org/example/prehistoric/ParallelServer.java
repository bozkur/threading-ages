package org.example.prehistoric;

import java.io.IOException;

/**
 * @author cevher
 */
public class ParallelServer extends AbstractServer {
    public ParallelServer(int port) throws IOException {
        super(port);
    }

    public void run() throws IOException {
        while (!server.isClosed()) {
            var socket = server.accept();
            new Thread(() -> handleRequest(socket)).start();
        }
    }
}
