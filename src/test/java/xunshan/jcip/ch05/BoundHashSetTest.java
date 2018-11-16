package xunshan.jcip.ch05;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public class BoundHashSetTest {

    private BoundHashSet<Integer> boundHashSet;

    @Before
    public void setUp() throws Exception {
        boundHashSet = new BoundHashSet<>(4);
    }

    @Test(expected = TimeoutException.class)
    public void bounded() throws InterruptedException, TimeoutException, ExecutionException {
        boolean res = boundHashSet.add(1);
        assertTrue(res);
        boundHashSet.add(2);
        boundHashSet.add(3);
        res = boundHashSet.add(4);
        assertTrue(res);

        final FutureTask<Boolean> future = new FutureTask<>(() -> boundHashSet.add(5));
        // because permits are used out, so semaphore.acquire will be blocked until other
        // operations released permits
        future.get(10, TimeUnit.SECONDS);
    }

    @Test
    public void release() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        boundHashSet.add(1);
        boundHashSet.add(2);
        boundHashSet.add(3);
        boundHashSet.add(4);

        SimpleThreadUtils.run(() -> {
            try {
                System.out.println("start add 5");
                boolean res = boundHashSet.add(5);
                System.out.println("add 5:" + res);
                assertTrue(res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(3000);
            boolean res = boundHashSet.remove(3);
            assertTrue(res);
            latch.countDown();
        });

        latch.await();
    }
}