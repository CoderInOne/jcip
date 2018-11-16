package xunshan.jcip.ch05;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

public class TestHarness {
    public static long taskTime(final Runnable task, int nThreads) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate   = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " preparing");
                    startGate.await();
                    System.out.println(Thread.currentThread().getName() + " running");
                    task.run();
                    System.out.println(Thread.currentThread().getName() + " finished");
                    endGate.countDown();
                } catch (InterruptedException e) { }
            }).start();
        }

        System.out.println("waiting in main thread");
        SimpleThreadUtils.sleepWithoutInterrupt(3000);

        long s = System.nanoTime();
        startGate.countDown();
        System.out.println("start all computing");
        endGate.await();
        System.out.println("all task end");
        long e = System.nanoTime();

        return e - s;
    }
}
