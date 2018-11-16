package xunshan.jcip.ch06;

import xunshan.jcip.util.SimpleThreadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    private void handleRequest(Socket connection) throws IOException {
        final InputStream is = connection.getInputStream();
        final OutputStream os = connection.getOutputStream();
        while (connection.isConnected()) {
            final int available = is.available();

            if (available <= 0) {
                SimpleThreadUtils.sleepWithoutInterrupt(100);
                continue;
            }

            // read data
            byte[] data = new byte[available];
            is.read(data);
            System.out.println(new String(data, "UTF-8"));

            os.write("hello".getBytes());
        }

        is.close();
        os.close();
    }

    @Override
    public void run() {
        try {
            handleRequest(socket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
