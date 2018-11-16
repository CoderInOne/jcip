package xunshan.jcip.basic;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

// lock can be changed, which is dangerous
public class NonFinalLock {
    private Object lock = new Object();

    private void foo() {
        System.out.println(Thread.currentThread().getName() + " try to acquire");
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " acquired");
            SimpleThreadUtils.sleepSilently(1000);
            System.out.println(Thread.currentThread().getName() + " release");
        }
    }

    private void bar() {
        lock = new Object();
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(3);
        final NonFinalLock lockChange = new NonFinalLock();
        SimpleThreadUtils.run(() -> {
            lockChange.foo();
            latch.countDown();
        });
        SimpleThreadUtils.run(() -> {
            lockChange.foo();
            latch.countDown();
        });
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(100);
            lockChange.bar();
            lockChange.foo();
            latch.countDown();
        });
        latch.await();
    }
}
