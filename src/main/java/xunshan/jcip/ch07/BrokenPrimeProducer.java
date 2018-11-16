package xunshan.jcip.ch07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

// queue.doPut(p) blocked if size is full and no consumer doTake them
// when queue.doPut(p) block, call cancel will cause block forever
// because there is no way to preemptively stop a thread in Java
public class BrokenPrimeProducer implements Runnable {
    private volatile boolean cancelled = false;
    private BlockingQueue<BigInteger> queue;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled) {
                queue.put(p.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        this.cancelled = true;
    }
}
