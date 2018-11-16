package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class VectorTest {
    private Integer getLast(Vector<Integer> list) {
        if (!list.isEmpty()) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
        else {
            return null;
        }
    }

    private Integer getLastSync(Vector<Integer> list) {
        synchronized (list) {
            return getLast(list);
        }
    }

    private void deleteLastSync(Vector<Integer> list) {
        synchronized (list) {
            deleteLast(list);
        }
    }

    private void deleteLast(Vector<Integer> list) {
        if (!list.isEmpty()) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        final Vector<Integer> list = new Vector<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        SimpleThreadUtils.run(() -> {
            try {
                while (!list.isEmpty()) {
                    getLast(list);
                }
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        SimpleThreadUtils.run(() -> {
            while (!list.isEmpty()) {
                deleteLast(list);
            }
            latch.countDown();
        });

        latch.await();
    }
}
