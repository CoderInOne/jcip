package xunshan.jcip.ch12;

import org.junit.Test;
import xunshan.jcip.base.ConsumerProducerTestEngine;
import xunshan.jcip.util.ThrowingConsumer;
import xunshan.jcip.util.ThrowingSupplier;

import java.util.concurrent.BrokenBarrierException;

import static org.junit.Assert.*;

public class BoundedBufferTest {
    /****************** basic ******************/
    @Test
    public void emptyWhenCreated() {
        final BoundedBuffer<Integer> buffer = new BoundedBuffer<>(10);
        assertTrue(buffer.isEmpty());
        assertFalse(buffer.isFull());
    }

    @Test
    public void full() throws InterruptedException {
        final BoundedBuffer<Integer> buffer = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) {
            buffer.put(i);
        }
        assertTrue(buffer.isFull());
        assertFalse(buffer.isEmpty());
    }


    /****************** blocking ******************/
    @Test
    public void blockingWhenTakeFromEmptyBuffer() {
        final BoundedBuffer<Integer> buffer = new BoundedBuffer<>(10);
        Thread takerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buffer.take();
                    fail();
                } catch (InterruptedException e) {
                }
            }
        });

        try {
            takerThread.start();
            Thread.sleep(1000);
            takerThread.interrupt();
            takerThread.join(1000);
            assertFalse(takerThread.isAlive());
            assertTrue(buffer.isEmpty());
        } catch (InterruptedException e) {
            fail();
        }
    }

    /****************** test safety ******************/
    @Test(timeout = 5000)
    public void testSafety() throws BrokenBarrierException, InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(100);

        ConsumerProducerTestEngine<Integer> testEngine = new ConsumerProducerTestEngine.Param<Integer>()
                .setnThreads(10)
                .setnTrials(10000)
                .setConsumer(ThrowingConsumer.wrap(buffer::put))
                .setSupplier(ThrowingSupplier.wrap(buffer::take))
                .setSeedValueMapper(seed -> seed)
                .setValueSeedMapper(seed -> seed)
                .build();

        boolean res = testEngine.start();
        assertTrue(res);

        testEngine.shutdown();
    }

    /******************* perf test *******************/
    @Test
    public void throughputTest() throws BrokenBarrierException, InterruptedException {
        final int trials = 1000000;
        for (int cap = 100; cap <= 100; cap *= 10) {
            System.out.println("-------- Capacity:" + cap + "----------");
            for (int pairs = 1; pairs <= 256; pairs *= 2) {
                test(trials, pairs, cap);
            }
            System.out.println("\n\n");
        }
    }

    private void test(int trials, int pairs, int cap) throws BrokenBarrierException, InterruptedException {
        final int p = pairs;
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(cap);

        ConsumerProducerTestEngine<Integer> testEngine = new ConsumerProducerTestEngine.Param<Integer>()
                .setnThreads(pairs)
                .setnTrials(trials)
                .setConsumer(ThrowingConsumer.wrap(buffer::put))
                .setSupplier(ThrowingSupplier.wrap(buffer::take))
                .setSeedValueMapper(seed -> seed)
                .setValueSeedMapper(seed -> seed)
                .setFinishedConsumer(t -> {
                    // total_items = #threads(pair) * trial
                    long nsPerItem = t / (p * trials);
                    System.out.println("Pairs: " + p + "\t Throughput:" + nsPerItem + " ns/item");
                })
                .build();

        boolean res = testEngine.start();
        assertTrue(res);

        testEngine.shutdown();
    }
}
