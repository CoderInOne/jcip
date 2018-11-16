package xunshan.jcip.ch12;

import org.junit.Test;
import xunshan.jcip.base.ConsumerProducerTestEngine;
import xunshan.jcip.util.SimpleThreadUtils;
import xunshan.jcip.util.ThrowingConsumer;
import xunshan.jcip.util.ThrowingSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static junit.framework.TestCase.assertTrue;

public class QueuePerfTest {
    @Test
    public void compareMultipleQueueImpl() throws BrokenBarrierException, InterruptedException {
        BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(256);
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(256);
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(256);

        int t = 1024 * 64;
        System.out.println("---------- BoundedBuffer -----------");
        test(t, ThrowingConsumer.wrap(boundedBuffer::put), ThrowingSupplier.wrap(boundedBuffer::take));
        SimpleThreadUtils.sleepSilently(1000);

        System.out.println("---------- ArrayBlockingQueue -----------");
        test(t, ThrowingConsumer.wrap(arrayBlockingQueue::put), ThrowingSupplier.wrap(arrayBlockingQueue::take));
        SimpleThreadUtils.sleepSilently(1000);

        System.out.println("---------- LinkedBlockingQueue -----------");
        test(t, ThrowingConsumer.wrap(linkedBlockingQueue::put), ThrowingSupplier.wrap(linkedBlockingQueue::take));
    }

    private void test(int t, Consumer<Integer> c, Supplier<Integer> s) throws BrokenBarrierException, InterruptedException {
        for (int p = 1; p <= 128; p *= 2) {
            final int pair = p;

            ConsumerProducerTestEngine<Integer> engine = new ConsumerProducerTestEngine.Param<Integer>()
                    .setConsumer(c)
                    .setSupplier(s)
                    .setnThreads(p)
                    .setnTrials(t)
                    .setSeedValueMapper(seed -> seed)
                    .setValueSeedMapper(seed -> seed)
                    .setFinishedConsumer(time -> {
                        long nPerItem = time / (t * pair);
                        System.out.println("Pair:" + pair + ", Throughput:" + nPerItem);
                    })
                    .build();
            boolean res = engine.start();
            assertTrue(res);

            engine.shutdown();
        }
    }
}
