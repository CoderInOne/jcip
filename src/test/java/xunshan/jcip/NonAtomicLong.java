package xunshan.jcip;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static xunshan.jcip.util.SimpleThreadUtils.run;
import static xunshan.jcip.util.SimpleThreadUtils.sleepWithoutInterrupt;

// try to proof 64-bit long is nonatomic, see jcip p36
// result: fail
public class NonAtomicLong {
    private long value;

    // for visibility
    private volatile long c = 100000000L;
    @Test
    public void long64bit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        run(() -> {
            boolean flag = false;
            while (c-- > 0) {
                value = flag ? Long.MAX_VALUE : 0;
                flag = !flag;
            }
            System.out.println("t1 finished");
            latch.countDown();
        });

        run(() -> {
            sleepWithoutInterrupt(100);
            while (c > 0) {
                // avoid changed value when execute tail part of &&
                long v = value;
                if (v != 0 && v != Long.MAX_VALUE) {
                    latch.countDown();
                    throw new RuntimeException("counter is " + c);
                }
            }
            System.out.println("t2 finished");
            latch.countDown();
        });

        latch.await();
        System.out.println("finished");
    }
}
