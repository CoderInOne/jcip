package xunshan.jcip.ch14;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SemaphoreOnLockTest {
    @Test
    public void basic() throws InterruptedException {
        final SemaphoreOnLock sem = new SemaphoreOnLock(3);
        final ScheduledExecutorService sched = Executors.newScheduledThreadPool(4);
        sched.schedule(() -> {
            try {
                sem.acquire();
                System.out.println("t1 acquire");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail();
            } finally {
                sem.release();
            }
        }, 0, TimeUnit.SECONDS);
        sched.schedule(() -> {
            try {
                sem.acquire();
                System.out.println("t2 acquire");

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail();
            }
        }, 100, TimeUnit.MILLISECONDS);
        sched.schedule(() -> {
            try {
                sem.acquire();
                System.out.println("t3 acquire");

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail();
            }
        }, 100, TimeUnit.MILLISECONDS);
        sched.schedule(() -> {
            try {
                sem.acquire();
                System.out.println("t4 acquire");

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail();
            }
        }, 100, TimeUnit.MILLISECONDS);

        Thread.sleep(3000);
    }

    @Test
    public void block() {
        final SemaphoreOnLock sem = new SemaphoreOnLock(1);
        Thread t = new Thread(() -> {
            try {
                sem.acquire();
                sem.acquire();
                fail();
            } catch (InterruptedException e) {
                System.out.println("interrupt event!");
            }
        });
        t.start();

        SimpleThreadUtils.sleepSilently(1000);
        t.interrupt();
    }
}