package xunshan.jcip.ch10;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class LeftRightDeadLockTest {

    // junit框架如何驱使latch.await();抛出异常
    @Test
    public void deadLock() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        final LeftRightDeadLock app = new LeftRightDeadLock();
        SimpleThreadUtils.run(() -> {
            app.leftRight();
            latch.countDown();
        });
        SimpleThreadUtils.run(() -> {
            app.rightLeft();
            latch.countDown();
        });
        latch.await();
    }

    @Test(timeout = 1000)
    public void interruptNonSensiableTimeout() {
        while (true) {

        }
    }
}