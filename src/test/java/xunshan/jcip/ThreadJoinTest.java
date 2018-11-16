package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import static org.junit.Assert.assertTrue;

public class ThreadJoinTest {
    /**
     * Waits for this thread to die.
     *
     * @throws  InterruptedException
     *          if any thread has interrupted the current thread. The
     *          <i>interrupted status</i> of the current thread is
     *          cleared when this exception is thrown.
     */
    @Test
    public void join() {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        System.out.println("running");
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    System.out.println("thread interrupted status:" + Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                }
            }
        });

        t.start();

        SimpleThreadUtils.run(new Runnable() {
            @Override
            public void run() {
                SimpleThreadUtils.sleepWithoutInterrupt(3000);
                t.interrupt();
            }
        });

        try {
            System.out.println("before join");
            t.join();
            System.out.println("after join");
        } catch (InterruptedException e) {
            System.out.println("join interrupted");
            e.printStackTrace();
        }
    }

    /**
     * while (isAlive()) {
     *     wait(0); // -> InterruptedException
     * }
     */
    @Test(expected = InterruptedException.class)
    public void whenJoinThrowInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        Thread.currentThread().join();
    }

    @Test
    public void joinByObjectWait() {
        final Thread mainThread = Thread.currentThread();
        WaitThread waitThread = new WaitThread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        System.out.println("running");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("catch interrupted");
                    Thread.currentThread().interrupt();
                }
            }
        });

        waitThread.start();
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(10000);
            waitThread.interrupt();
            SimpleThreadUtils.sleepWithoutInterrupt(10000);
            //mainThread.interrupt();
        });

        try {
            waitThread.myJoin();
        } catch (InterruptedException e) {
            System.out.println("join throw interrupted exception");
        }
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void waitObject() throws InterruptedException {
        Object o = new Object();
        o.wait();
    }

    @Test
    public void waitObjectInMultipleThread() {
        final WaitObject waitObject = new WaitObject(1000);
        final Thread t1 = new Thread(waitObject);

        t1.start();
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(3000);
            t1.interrupt();
        });

        try {
            t1.join();
        }
        catch (InterruptedException e) { }
        finally {
            assertTrue(waitObject.getThrowable() instanceof InterruptedException);
        }
    }

    @Test
    public void waitInSameThread() {
        final WaitObject waitObject = new WaitObject(100);
        Thread t = Thread.currentThread();
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(3000);
            t.interrupt();
        });
        waitObject.run();
        final Throwable throwable = waitObject.getThrowable();
        assertTrue(throwable instanceof InterruptedException);
    }

    private class WaitObject implements Runnable {
        private long t;
        private Throwable th;

        public WaitObject(long t) {
            this.t = t;
        }

        // synchronized is key to get monitor of object
        @Override
        public synchronized void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    wait(t);
                }
            } catch (InterruptedException | IllegalMonitorStateException e) {
                this.th = e;
            }
        }

        public Throwable getThrowable() {
            return this.th;
        }
    }

    private class WaitThread extends Thread {
        private Object waitObject = new Object();

        public WaitThread(Runnable target) {
            super(target);
        }

        // call from other thread
        void myJoin() throws InterruptedException {
            // get monitor for wait
            synchronized (waitObject) {
                // test this thread is alive
                while (isAlive()) {
                    /**
                     * java.lang.IllegalMonitorStateException
                     * 	at java.lang.Object.wait(Native Method)
                     * 	at xunshan.jcip.ThreadJoinTest$WaitThread.myJoin(ThreadJoinTest.java:98)
                     * 	at xunshan.jcip.ThreadJoinTest.joinByObjectWait(ThreadJoinTest.java:83)
                     *
                     * 	Design rules:
                     * 	1. The goal of join is waiting for this thread to die, so only Thread object
                     * 	can listen InterruptedException of wait
                     */
                    System.out.println("waiting");
                    waitObject.wait(1000);
                }
            }
        }
    }
}
