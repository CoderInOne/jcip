package xunshan.jcip.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8990);
        while (true) {
            Socket connection = serverSocket.accept();
            new RequestHandler(connection).run();
        }
    }
}
