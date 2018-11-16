package xunshan.jcip;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.fail;

public class ConcurrentHashMapTest {
    @Test
    public void updateVisibleForGet() {
        fail();

    }

    @Test
    public void equalWhileUpdating() {
        fail();
    }

    /**
     * times:10^7, when times <= 10^6, their performance is very close
     * |   operation n      |   synchronized map   |  ConcurrentHashMap   |
     * |       1            |          13173       |         5957         |
     * |       2            |          11766       |         5867         |
     * |       3            |          12257       |         6556         |
     * |       4            |          12046       |         10483        |
     * |       5            |          14612       |         8782         |
     */
    @Test
    public void accessPerfWithSyncMap() {
        final int t = 20000000;
        final Map<Integer, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
        final Map<Integer, Integer> conhMap = new ConcurrentHashMap<>();
        final CountDownLatch outerLatch = new CountDownLatch(2);
        getMapOperationTime(t, conhMap, outerLatch);
        getMapOperationTime(t, syncMap, outerLatch);
        await(outerLatch);
    }

    @Test
    public void iterate() {
        fail();
    }

    /**
     * read and write specific times
     * @param t operation time
     */
    private void getMapOperationTime(int t, Map<Integer, Integer> map, CountDownLatch outerLatch) {
        run(() -> {
            final CountDownLatch latch = new CountDownLatch(4);
            long s = System.currentTimeMillis();
            // write thread
            run(() -> {
                for (int i = 0; i < t; i++) {
                    map.put(i, i);
                }
                latch.countDown();
            });

            // read threads
            run(() -> {
                for (int i = 0; i < t; i++) {
                    map.get(i);
                }
                latch.countDown();
            });
            run(() -> {
                for (int i = 0; i < t; i++) {
                    map.get(i);
                }
                latch.countDown();
            });
            run(() -> {
                for (int i = 0; i < t; i++) {
                    map.get(i);
                }
                latch.countDown();
            });
            await(latch);
            long e = System.currentTimeMillis();
            System.out.println(map instanceof ConcurrentHashMap ?
                    "ConcurrentHashMap:" + (e - s) :
                    "synchronizedMap:" + (e - s)
            );
            outerLatch.countDown();
        });
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run(Runnable r) {
        new Thread(r).start();
    }
}
