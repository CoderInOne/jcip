package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.ReflectionUtils;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class CopyOnWriteArrayListTest {

    private List<Integer> list;

    @Before
    public void setUp() throws Exception {
        list = new CopyOnWriteArrayList<>();
    }

    @Test
    public void iterate() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        CountDownLatch latch = new CountDownLatch(2);

        // iterator is snapshot of list, so list.remove will not
        // affect iterator at all
        final Iterator<Integer> it = list.iterator();
        SimpleThreadUtils.run(() -> {
            int counter = 0;
            while (it.hasNext()) {
                it.next();
                counter++;
            }
            latch.countDown();
            System.out.println("t1:counter=" + counter);
        });

        SimpleThreadUtils.run(() -> {
            while (it.hasNext()) {
                list.remove(list.size() - 1);
                // COWIterator do not support remove
                // it.remove();
            }
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void onWriteCopy() {
        list.add(1);
        Object array = ReflectionUtils.getFieldByName(list, "array");
        list.get(0);
        Object array1 = ReflectionUtils.getFieldByName(list, "array");
        assertSame(array, array1);

        list.add(2);
        Object array2 = ReflectionUtils.getFieldByName(list, "array");
        assertNotSame(array, array2);
    }
}
