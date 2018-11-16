package xunshan.jcip.ch13;

import org.junit.Test;
import org.mockito.Mockito;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReentrantLockTest {
    @Test(timeout = 1000)
    public void tryLockReturnFastWhenLockHeld() {
        final ReentrantLock lock = new ReentrantLock();
        final CountDownLatch latch = new CountDownLatch(1);
        lock.lock();
        try {
            SimpleThreadUtils.run(() -> {
                if (lock.tryLock()) {
                    fail();
                } else {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (InterruptedException e) {
            fail();
        } finally {
            lock.unlock();
        }
    }

    @Test(timeout = 3000)
    public void lockWithBudget() {
        final CountDownLatch latch = new CountDownLatch(2);
        long lockTimeout = 1; /* second */
        Function<String, Boolean> func = Mockito.mock(Function.class);
        Lock lock = new ReentrantLock();
        String t = "hi";
        when(func.apply(t)).thenReturn(Boolean.TRUE);
        try {
            // hold lock for 1.5 s
            SimpleThreadUtils.run(() -> {
               lock.lock();
               try {
                   SimpleThreadUtils.sleepSilently(2000);
               } finally {
                   lock.unlock();
                   latch.countDown();
               }
            });

            SimpleThreadUtils.sleepSilently(100);
            boolean res = doActionWithTimeBudget(lock, func, t, lockTimeout, TimeUnit.SECONDS);
            assertFalse(res);
            verify(func, never()).apply(t);

            // hold lock for 0.5 s
            SimpleThreadUtils.run(() -> {
                lock.lock();
                try {
                    SimpleThreadUtils.sleepSilently(500);
                } finally {
                    lock.unlock();
                    latch.countDown();
                }
            });

            // success lock
            doActionWithTimeBudget(lock, func, t, lockTimeout, TimeUnit.SECONDS);
            verify(func).apply(t);

            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test(timeout = 5000)
    public void lockInterruptibly() {
        final ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                try {
                    // do action
                    while (true) {}
                }
                finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        SimpleThreadUtils.sleepSilently(2000);
        t.interrupt();

        SimpleThreadUtils.sleepSilently(2000);
    }

    @Test
    public void lockInterruptiblyFailWhenInterrupted() {
        Lock lock = new ReentrantLock();
        Thread.currentThread().interrupt();
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            return;
        }
        fail();
    }

    private <T> boolean doActionWithTimeBudget(
            Lock lock, Function<T, Boolean> func, T t,
            long lockTimeout, TimeUnit unit) throws InterruptedException {
        if (!lock.tryLock(lockTimeout, unit)) {
            return false;
        }
        try {
            return func.apply(t);
        } finally {
            lock.unlock();
        }
    }
}
