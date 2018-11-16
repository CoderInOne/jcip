package xunshan.jcip.ch14;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.base.ConsumerProducerTestEngine;
import xunshan.jcip.util.ThrowingConsumer;
import xunshan.jcip.util.ThrowingSupplier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class BaseBufferTest {
    private ScheduledExecutorService sched = Executors.newScheduledThreadPool(2);
    protected BaseBoundedBuffer<Integer> buffer;

    @Before
    public void setUp() throws Exception {
        buffer = getBufferImpl();
    }

    protected abstract BaseBoundedBuffer<Integer> getBufferImpl();

    @Test
    public void basic() throws InterruptedException {
        buffer.put(1);
        assertEquals(1, buffer.take().intValue());

        buffer.put(2);
        buffer.put(3);
        buffer.put(4);

        assertEquals(2, buffer.take().intValue());
        assertEquals(3, buffer.take().intValue());
        assertEquals(4, buffer.take().intValue());
    }

    @Test
    public void block() {
        final Thread mainThread = Thread.currentThread();
        sched.schedule(mainThread::interrupt, 1, TimeUnit.SECONDS);

        try {
            buffer.take();
            fail();
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Test
    public void putBlock() {
        final Thread mainThread = Thread.currentThread();
        sched.schedule(mainThread::interrupt, 1, TimeUnit.SECONDS);
        int c = buffer.capacity();
        try {
            while (--c >= 0) {
                buffer.put(c);
            }
        } catch (InterruptedException e) {
            fail();
        }

        try {
            buffer.put(1);
            fail();
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Test
    public void safety() throws BrokenBarrierException, InterruptedException {
        ConsumerProducerTestEngine<Integer> engine =
                new ConsumerProducerTestEngine.Param<Integer>()
                        .setnThreads(10)
                        .setnTrials(10000)
                        .setSupplier(ThrowingSupplier.wrap(() -> {
                            Integer i = buffer.take();
                            if (i == null) {
                                fail();
                            }
                            return i;
                        }))
                        .setConsumer(ThrowingConsumer.wrap(t -> {
                            if (t == null) {
                                fail();
                            }
                            buffer.put(t);
                        }))
                        .setSeedValueMapper(seed -> seed)
                        .setValueSeedMapper(seed -> seed)
                        .setFinishedConsumer(ts -> {
                            long nsPerItem = ts / (10 * 10000);
                            System.out.println("Pairs:" + 10 + "\tTrials:" + 10000
                                  + "\t" + buffer.getClass().getName() + "-Throughput:" + nsPerItem);
                        })
                        .build();

        boolean res = engine.start();
        assertTrue(res);
        engine.shutdown();
    }
}
