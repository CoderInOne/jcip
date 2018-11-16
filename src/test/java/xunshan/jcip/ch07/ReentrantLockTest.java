package xunshan.jcip.ch07;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    // how reentrant lock response to interrupt
    @Test
    public void lockInterruptiblyHack() {
        final FooThread fooThread = new FooThread();
        fooThread.start();
        SimpleThreadUtils.sleepSilently(2000);
        fooThread.interrupt();
        SimpleThreadUtils.sleepSilently(2000);
    }

    @Test
    public void condition() {

    }

    private class FooThread extends Thread {
        private Foo foo = new Foo();
        @Override
        public void run() {
            try {
                while (true) {
                    SimpleThreadUtils.sleepSilently(100);
                    foo.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Foo {
        private final ReentrantLock takeLock = new ReentrantLock();
        private final Condition notEmpty = takeLock.newCondition();

        public void run() throws InterruptedException {
            // do not response directly
            takeLock.lockInterruptibly();

            try {
                System.out.println("do something");
                // throw interrupted exception when waiting
                notEmpty.await(100, TimeUnit.MILLISECONDS);
            } finally {
                takeLock.unlock();
            }
        }
    }
}
