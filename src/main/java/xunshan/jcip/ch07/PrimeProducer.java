package xunshan.jcip.ch07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

public class PrimeProducer extends Thread {
    private BlockingQueue<BigInteger> queue;
    private UncaughtExceptionHandler handler;

    public PrimeProducer(
            BlockingQueue<BigInteger> queue,
            UncaughtExceptionHandler handler) {
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
            handler.uncaughtException(this, e);
        }
    }

    public void cancel() {
        interrupt();
        assert isInterrupted();
    }
}
