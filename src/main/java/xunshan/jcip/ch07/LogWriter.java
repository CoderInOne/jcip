package xunshan.jcip.ch07;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class LogWriter {
    private final BlockingQueue<String> queue;
    private final LogThread logThread;
    private volatile boolean shutdownRequested = false;

    public LogWriter(BlockingQueue<String> queue) {
        this.queue = queue;
        this.logThread = new LogThread();
    }

    public void start() {
        logThread.start();
    }

    public void put(String msg) throws InterruptedException {
        // risk: race condition for check-and-act
        // TODO test
        if (!shutdownRequested) {
            queue.put(msg);
        } else {
            throw new IllegalStateException("logger is shut down!");
        }
    }

    public void stop() {
        logThread.interrupt();
        shutdownRequested = true;
    }

    class LogThread extends Thread {
        private final OutputStream printWriter;

        public LogThread() {
            this.printWriter = System.out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    final String msg = queue.take();
                    System.out.println(msg);
                }
            } catch (InterruptedException e) {
                System.err.println("interrupt consumer");
                interrupt();
            } finally {
                // printWriter.close();
            }
        }
    }
}
