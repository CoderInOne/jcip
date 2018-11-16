package xunshan.jcip.ch07;

import org.junit.Test;
import org.mockito.Mockito;
import xunshan.jcip.util.SimpleThreadUtils;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class PrimeProducerTest {

    @Test(timeout = 3000)
    public void brokenPrimeProducer() {
        BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>(3);
        final BrokenPrimeProducer producer = new BrokenPrimeProducer(queue);
        new Thread(producer).start();
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        producer.cancel();
    }

    @Test
    public void primeProducer() {
        BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>(3);
        final Thread.UncaughtExceptionHandler handler =
                Mockito.mock(Thread.UncaughtExceptionHandler.class);
        final PrimeProducer producer = new PrimeProducer(queue, handler);
        producer.start();
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        producer.cancel();
        verify(handler).uncaughtException(any(Thread.class), any(InterruptedException.class));
    }

    @Test
    public void interInRunnablePrimeProducer() {
        IllegalTask illegalTask = new IllegalTask();
        SimpleThreadUtils.run(illegalTask);
        illegalTask.cancel();
        assertTrue(illegalTask.done);
    }

    // Thread.interrupted() clear interrupted status
    @Test
    public void interruptedMethodClearInterruptedStatus() {
        Thread.currentThread().interrupt();
        boolean res = Thread.interrupted();
        assertTrue(res);
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
    }

    @Test(expected = RuntimeException.class)
    public void throwInterruptedExceptionAfterInterrupt() {
        Thread.currentThread().interrupt();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class IllegalTask implements Runnable {
        private boolean done = false;

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("running");
                }
                done = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void cancel() {
            // here Thread.currentThread() != IllegalTask thread
            Thread.currentThread().interrupt();
        }
    }
}