package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.fail;

public class ObjectWaitTest {
    /**
     * requirement: thread MUST own this object's monitor
     *
     * 1. current thread wait until 4 events raise
     * 2. relinquish any or all synchronization on this object
     *
     * 4 events:
     * [1] notify by other thread, if more than one thread,
     *     only one thread is selected by randomly
     * [2] notifyAll
     * [3] interrupt() call by that thread
     * [4] timeout
     */
    @Test
    public void twoThreadWaitInSameObject() {
        final Foo foo = new Foo();
        SimpleThreadUtils.run(foo::run);
        SimpleThreadUtils.run(foo::run);
        SimpleThreadUtils.run(foo::run);

        SimpleThreadUtils.sleepSilently(3000);
        // needs monitor
        synchronized (foo) {
            foo.notifyAll();
        }
        SimpleThreadUtils.sleepSilently(3000);
    }

    private class Foo {
        void run() {
            System.out.println(Thread.currentThread().getName()
                    + ":before sync");
            synchronized (this) {
                System.out.println(Thread.currentThread().getName()
                        + ":in sync");
                try {
                    wait();
                    System.out.println(Thread.currentThread().getName()
                            + ":pass wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void wait1() throws InterruptedException {
        final Object lock = new Object();
        final CountDownLatch latch = new CountDownLatch(3);
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("t1 sleep 5 second");
                    SimpleThreadUtils.sleepSilently(5000);
                } catch (InterruptedException e) {
                    fail();
                } finally {
                    System.out.println("someone notify t1");
                    latch.countDown();
                }
            }
        });
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("t2 sleep 3 second");
                    SimpleThreadUtils.sleepSilently(3000);
                } catch (InterruptedException e) {
                    fail();
                } finally {
                    System.out.println("someone notify t2");
                    latch.countDown();
                }
            }
        });
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            synchronized (lock) {
                SimpleThreadUtils.sleepSilently(1000);
                lock.notifyAll();
                System.out.println("after notify");
                latch.countDown();
                System.out.println("before release lock");
                SimpleThreadUtils.sleepSilently(5000);
                System.out.println("release lock");
            }
        });
        latch.await();
    }
}
