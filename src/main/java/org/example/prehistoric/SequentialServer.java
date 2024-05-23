package org.example.prehistoric;


import org.example.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author cevher
 */
public class SequentialServer extends AbstractServer{

    public SequentialServer(int port) throws IOException {
        super(port);
    }

    public void run() throws IOException {
        while (!server.isClosed()) {
            var socket = server.accept();
            handleRequest(socket);
        }
    }
}
