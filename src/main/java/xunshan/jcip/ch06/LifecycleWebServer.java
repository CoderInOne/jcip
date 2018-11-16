package xunshan.jcip.ch06;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class LifecycleWebServer {
    private static final ExecutorService exec = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8990);
        while (!exec.isShutdown()) {
            try {
                System.out.println("waiting request");
                // Socket.accept: without timeout, may block here even if executor is shut down
                final Socket connection = serverSocket.accept();
                System.out.println("got connection " + connection);
                exec.execute(() -> {
                    try {
                        final InputStream is = connection.getInputStream();
                        while (!exec.isShutdown()) {
                            final int len = is.available();

                            if (len <= 0) {
                                continue;
                            }

                            byte[] data = new byte[len];
                            is.read(data);
                            String s = new String(data, "UTF-8");
                            if (s.length() > 5) { // mock stop cmd
                                System.out.println("shutting down");
                                exec.shutdown();
                                break;
                            } else {
                                System.out.println(Thread.currentThread().getName() + " got " + s);
                            }
                        }

                        System.out.println(Thread.currentThread().getName() + " exit");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RejectedExecutionException e1) {
                if (!exec.isShutdown()) {
                    // should not be here, because executor is shut down already
                }
            }
        }
    }
}
