package xunshan.jcip;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class NoVisibilityTest {
    /*volatile*/ boolean flag = true;

    @Test(timeout = 2000)
    public void nonStop() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            while (flag) { // can not see newest flag
                // do nothing
            }
            latch.countDown();
        }).start();

        Thread.sleep(500);
        flag = false;

        latch.await();
    }
}