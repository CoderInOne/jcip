package xunshan.jcip.ch10;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

public class LeftRightWithTryLockTest {

    // junit框架如何驱使latch.await();抛出异常
    @Test(timeout = 5000)
    public void deadLock() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        final LeftRightWithTryLock app = new LeftRightWithTryLock();
        SimpleThreadUtils.run(() -> {
            try {
                app.leftRight();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        SimpleThreadUtils.run(() -> {
            app.rightLeft();
            latch.countDown();
        });
        latch.await();
    }
}