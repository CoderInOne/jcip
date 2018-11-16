package xunshan.jcip.ch05;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.sql.SQLOutput;

import static org.junit.Assert.*;

public class MyCountDownLatchTest {
    @Test(timeout = 2000)
    public void basic() {
        MyCountDownLatch latch = new MyCountDownLatch(1);
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void waitTest() {

        final Object lock = new Object();
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                System.out.println("got lock in t1");
                // SimpleThreadUtils.sleepSilently(3000);
                try {
                    SimpleThreadUtils.sleepSilently(3000);
                    lock.wait();
                    System.out.println("exit wait in t1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                System.out.println("got lock in t2");
                SimpleThreadUtils.sleepSilently(3000);
                lock.notify();
            }
        });
        SimpleThreadUtils.sleepSilently(9000);
    }

    @Test
    public void multipleThreadWait() {
        final Object lock = new Object();
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("after wait t1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("after wait t2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("after wait t3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            SimpleThreadUtils.sleepSilently(1000);
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    @Test
    public void interruptWhileAcquiring() {
        final Object lock = new Object();
        SimpleThreadUtils.run(() -> {
            synchronized (lock) {
                System.out.println("lock is held");
                SimpleThreadUtils.sleepSilently(100000);
            }
            System.out.println("lock is release");
        });
        final Thread t = SimpleThreadUtils.run(() -> {
            System.out.println("try acquire lock");
            try {
                // 强制停止怎么实现的
//                synchronized (lock) {
//                    System.out.println("got lock");
//                }
                while (true) {}
            } finally {
                System.out.println("t exit");
            }
        });
        SimpleThreadUtils.sleepSilently(100);
        System.out.println("interrupt t");
        t.interrupt();
        SimpleThreadUtils.sleepSilently(1000);
    }
}