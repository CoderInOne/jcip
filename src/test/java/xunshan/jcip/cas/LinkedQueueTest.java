package xunshan.jcip.cas;

import org.junit.Test;
import xunshan.jcip.base.ConsumerProducerTestEngine;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class LinkedQueueTest {
    @Test(timeout = 2000)
    public void basic() throws InterruptedException {
        LinkedQueue<Integer> queue = new LinkedQueue<>();

        CountDownLatch latch = new CountDownLatch(2);

        SimpleThreadUtils.run(() -> {
            assertEquals(1, queue.get().intValue());
            assertEquals(2, queue.get().intValue());
            assertEquals(3, queue.get().intValue());
            assertEquals(4, queue.get().intValue());
            latch.countDown();
        });

        SimpleThreadUtils.run(() -> {
            queue.put(1);
            queue.put(2);
            queue.put(3);
            queue.put(4);
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void safety() throws BrokenBarrierException, InterruptedException {
        LinkedQueue<Integer> queue = new LinkedQueue<>();

        ConsumerProducerTestEngine<Integer> engine = new ConsumerProducerTestEngine.Param<Integer>()
                .setnThreads(20)
                .setnTrials(10000)
                .setConsumer(queue::put)
                .setSupplier(queue::get)
                .setSeedValueMapper(seed -> seed)
                .setValueSeedMapper(v -> v == null ? 0 : v)
                .build();

        boolean res = engine.start();
        assertTrue(res);
        engine.shutdown();
    }
}