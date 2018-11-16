package xunshan.jcip.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPerRequestWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8990);
        while (true) {
            final Socket connection = serverSocket.accept();
            new Thread(new RequestHandler(connection)).start();
        }
    }
}
