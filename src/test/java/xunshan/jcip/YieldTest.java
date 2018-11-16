package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class YieldTest {
    @Test
    public void test() throws InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            exec.execute(this::doSomethind);
        }
        latch.await();
        exec.shutdownNow();
    }

    private AtomicInteger counter = new AtomicInteger();
    private CountDownLatch latch = new CountDownLatch(5);

    private synchronized void doSomethind() {
        System.out.println(counter.get() +  " begin");
        Thread.yield();

        SimpleThreadUtils.sleepSilently(5000);
        System.out.println(counter.get() + " end");
        counter.getAndIncrement();
        latch.countDown();
    }
}
