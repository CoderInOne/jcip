package xunshan.jcip.basic;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

// https://stackoverflow.com/questions/15438727/if-i-synchronized-two-methods-on-the-same-class-can-they-run-simultaneously
public class MethodsShareSameLock {
    private synchronized void methodA() {
        System.out.println("before a");
        SimpleThreadUtils.sleepWithoutInterrupt(9000);
        System.out.println("after a");
    }

    private synchronized void methodB() {
        System.out.println("before b");
        SimpleThreadUtils.sleepWithoutInterrupt(500);
        System.out.println("after b");
    }

    public static void main(String[] args) throws InterruptedException {
        MethodsShareSameLock syncMethods = new MethodsShareSameLock();
        CountDownLatch latch = new CountDownLatch(2);
        SimpleThreadUtils.run(() -> {
            syncMethods.methodA();
            latch.countDown();
        });
        SimpleThreadUtils.run(() -> {
            syncMethods.methodB();
            latch.countDown();
        });
        System.out.println("waiting to finished");
        latch.await();
    }
}
