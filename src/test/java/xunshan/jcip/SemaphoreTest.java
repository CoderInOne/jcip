package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    @Test(timeout = 2000)
    public void basic() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        SimpleThreadUtils.run(() -> {
            try {
                semaphore.acquire();
                SimpleThreadUtils.sleepSilently(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // semaphore.release();
            }
        });
        SimpleThreadUtils.run(() -> {
            try {
                semaphore.acquire();
                SimpleThreadUtils.sleepSilently(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // semaphore.release();
            }
        });

        SimpleThreadUtils.sleepSilently(1000);
        semaphore.acquire();
        System.out.println("acquired");
        semaphore.release();
    }
}
