package xunshan.jcip.ch12;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public class TestingThreadFactoryTest {
    @Test
    public void testPoolExpansion() {
        int size = 10;
        TestingThreadFactory fac = new TestingThreadFactory();
        ExecutorService executorService = new ThreadPoolExecutor(size, size,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), fac);
        for (int i = 0; i < 10 * size; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    SimpleThreadUtils.sleepSilently(10_000);
                }
            });
        }

        // no special suppose
        for (int i = 0; i < 20 && fac.threadCreated.get() < size; i++) {
            SimpleThreadUtils.sleepSilently(1000);
        }

        assertEquals(size, fac.threadCreated.get());
        executorService.shutdownNow();
    }
}