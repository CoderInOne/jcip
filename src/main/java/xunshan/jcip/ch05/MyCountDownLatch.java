package xunshan.jcip.ch05;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implement my countdown latch
 *
 * 问题：
 * 1. lock依赖synchronized，在其他线程没有lock#wait的情况下，只有一个能拿到
 * 2. 不能同时countDown
 */
public class MyCountDownLatch {
    private final Object lock = new Object();
    private final AtomicInteger counter;

    public MyCountDownLatch(int c) {
        this.counter = new AtomicInteger(c);
    }

    public void countDown() {
        synchronized (lock) {
            int c = counter.get();
            if (c == 0) {
                lock.notify();
                return;
            }

            counter.compareAndSet(c, c - 1);
            if (counter.get() == 0) {
                lock.notify();
            }
        }
    }

    public void await() {
        synchronized (lock) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
