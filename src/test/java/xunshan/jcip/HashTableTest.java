package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class HashTableTest {
    private Hashtable<Integer, String> ht;

    @Before
    public void setUp() throws Exception {
        ht = new Hashtable<>();
    }

    @Test
    public void failFast() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        ht.put(1, "one");
        ht.put(2, "two");
        ht.put(3, "thr");

        final Iterator<Map.Entry<Integer, String>> iterator = ht.entrySet().iterator();

        SimpleThreadUtils.run(() -> {
            try {
                while(iterator.hasNext()) {
                    SimpleThreadUtils.sleepWithoutInterrupt(1000);
                    iterator.next();
                    iterator.remove();
                    // System.out.println(iterator.next());
                }
            } finally {
                latch.countDown();
            }
        });

        SimpleThreadUtils.run(() -> {
            // SimpleThreadUtils.sleepWithoutInterrupt(1500);
            // SimpleThreadUtils.sleepWithoutInterrupt(500);
            // iterator.remove();
            latch.countDown();
        });

        latch.await();

        System.out.println(ht);
    }
}
