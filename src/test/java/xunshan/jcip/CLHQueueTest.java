package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static junit.framework.TestCase.fail;

// How to schedule await and count down
public class CLHQueueTest {
    private ThreadPoolExecutor executor;

    @Before
    public void setUp() throws Exception {
        executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), new ThreadFactory());
    }

    @Test
    public void howThreadsManaged() throws InterruptedException {
        Semaphore sp = new Semaphore(5);
        for (int i = 0; i < 8; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("acquire");
                    try {
                        sp.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // awake head's successor by unpark
        sp.release();

        sp.release();

        sp.acquire();
    }

    @Test
    public void ownerThreadIsHeadNode() throws InterruptedException {
        final PublicReentrantLock lock = new PublicReentrantLock();

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                lock.lock();
                try {
                    System.out.println(lock.getQueuedThreads());
                    Thread.sleep(1000);
                    System.out.println(lock.getQueuedThreads());
                } catch (Exception e) {
                    fail();
                } finally {
                    lock.unlock();
                    System.out.println(lock.getQueuedThreads());
                }
            });
        }

        Thread.sleep(5000);
    }

    @Test
    public void interruptQueueThread() throws InterruptedException {
        final PublicReentrantLock lock = new PublicReentrantLock();

        lock.lock();
        executor.submit(() -> {
            try {
                lock.tryLock(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                fail();
            }
        });

        Thread.sleep(500);
        System.out.println(lock.getQueuedThreads());
        Thread.sleep(1000);
        System.out.println(lock.getQueuedThreads());
        lock.unlock();
    }

    static class PublicReentrantLock extends ReentrantLock {
        public Collection<Thread> getWaitingThreads(Condition c) {
            return super.getWaitingThreads(c);
        }

        public Collection<Thread> getQueuedThreads() {
            return super.getQueuedThreads();
        }
    }

    private class ThreadFactory implements java.util.concurrent.ThreadFactory {
        private AtomicInteger counter = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            System.out.println("create thread");
            Thread t = new Thread(r);
            t.setName("T" + counter.getAndIncrement());
            return t;
        }
    }
}
