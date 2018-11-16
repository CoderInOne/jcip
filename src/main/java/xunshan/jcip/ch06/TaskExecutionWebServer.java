package xunshan.jcip.ch06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {
    private static Executor executor = Executors.newFixedThreadPool(100);
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8990);
        while (true) {
            final Socket connection = serverSocket.accept();
            executor.execute(new RequestHandler(connection));
        }
    }
}
