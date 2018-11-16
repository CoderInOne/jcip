package xunshan.jcip.ch07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

public class InterrucptInRunnableProducer implements Runnable {
    private BlockingQueue<BigInteger> queue;
    private Thread.UncaughtExceptionHandler handler;

    public InterrucptInRunnableProducer(
            BlockingQueue<BigInteger> queue,
            Thread.UncaughtExceptionHandler handler) {
        this.queue = queue;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            handler.uncaughtException(Thread.currentThread(), e);
        }
    }

    public void cancel() {
        Thread.currentThread().interrupt();
        assert Thread.currentThread().isInterrupted();
    }
}
