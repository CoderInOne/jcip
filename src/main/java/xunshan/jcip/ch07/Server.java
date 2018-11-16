package xunshan.jcip.ch07;

import xunshan.jcip.util.SimpleThreadUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        int counter = 10;
        while (counter-- > 0) {
            final Socket socket = serverSocket.accept();
            final ReaderThread readerThread = new ReaderThread(socket);

            readerThread.start();

            SimpleThreadUtils.sleepWithoutInterrupt(1000);

            readerThread.interrupt();
        }
    }
}
