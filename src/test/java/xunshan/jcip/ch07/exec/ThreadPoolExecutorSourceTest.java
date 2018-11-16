package xunshan.jcip.ch07.exec;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class ThreadPoolExecutorSourceTest {

    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    /**
     * {@link java.util.concurrent.ThreadPoolExecutor#ctl}
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    // RUNNING < 0
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    // Packing and unpacking ctl
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    /**
     * Attempts to CAS-increment the workerCount field of ctl.
     */
    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    @Test
    public void printAllState() {
        System.out.println("CAPACITY:  " + Integer.toBinaryString(CAPACITY));
        System.out.println("RUNNING:   " + Integer.toBinaryString(RUNNING));
        System.out.println("SHUTDOWN:  " + Integer.toBinaryString(SHUTDOWN));
        System.out.println("STOP:      " + Integer.toBinaryString(STOP));
        System.out.println("TIDYING:   " + Integer.toBinaryString(TIDYING));
        System.out.println("TERMINATED:" + Integer.toBinaryString(TERMINATED));
    }

    @Test
    public void stateAndWorkerCount() {
        int corePoolSize = 4;

        // RUNNING:           11111111111111111111111111111
        // workerCountOf:  11100000000000000000000000000000

        int c = ctl.get();
        System.out.println(Integer.toBinaryString(runStateOf(c)));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        if (workerCountOf(c) < corePoolSize) {
            // add worker
            // java.util.concurrent.ThreadPoolExecutor.addWorker
            // CAS will ensure old value is c, if not, someone changed it
            // then res = false; is yes, then change it to new value
            boolean res = ctl.compareAndSet(c, c + 1);
        }

        System.out.println("--------- run of state ---------");
        System.out.println(Integer.toBinaryString(runStateOf(ctl.get())));
        System.out.println(Integer.toBinaryString(SHUTDOWN));

        System.out.println("--------- SHUTDOWN state ---------");
        int v = ctlOf(SHUTDOWN, workerCountOf(ctl.get()));
        System.out.println(v);
        advanceRunState(SHUTDOWN);
        System.out.println(Integer.toBinaryString(runStateOf(ctl.get())));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        assertTrue(isShutdown());

        assertTrue(isTerminating());
        // every thread exit, call java.util.concurrent.ThreadPoolExecutor.tryTerminate
        decrementWorkerCount();
        tryTerminate();
        assertTrue(isTerminated());
    }

    public boolean isTerminated() {
        return runStateAtLeast(ctl.get(), TERMINATED);
    }

    public boolean isTerminating() {
        int c = ctl.get();
        return ! isRunning(c) && runStateLessThan(c, TERMINATED);
    }

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private boolean check(Runnable firstTask) {
        // Check if queue empty only if necessary.
        int rs = runStateOf(ctl.get());
        if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                        firstTask == null &&
                        ! workQueue.isEmpty()))
            return false;
        return true;
    }

    // set ctl as 0
    private void advanceRunState(int targetState) {
        for (;;) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) ||
                    ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
                break;
        }
    }

    final void tryTerminate() {
        for (;;) {
            int c = ctl.get();
            if (isRunning(c) ||
                    runStateAtLeast(c, TIDYING) ||
                    (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty())) {
                System.out.println("check return");
                return;
            }
            if (workerCountOf(c) != 0) { // Eligible to terminate
                // interruptIdleWorkers(ONLY_ONE);
                System.out.println("Eligible to terminate");
                return;
            }

            // final ReentrantLock mainLock = this.mainLock;
            // mainLock.lock();
            try {
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        // callback
                        // terminated();
                    } finally {
                        ctl.set(ctlOf(TERMINATED, 0));
                        System.out.println(ctl.get());
                        // termination.signalAll();
                    }
                    return;
                }
            } finally {
                // mainLock.unlock();
            }
            // else retry on failed CAS
        }
    }
    private void decrementWorkerCount() {
        do {} while (! ctl.compareAndSet(ctl.get(), ctl.get() - 1));
    }

/*  only signal thread to stop
    private void interruptIdleWorkers(boolean onlyOne) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (ThreadPoolExecutor.Worker w : workers) {
                Thread t = w.thread;
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        w.unlock();
                    }
                }
                if (onlyOne)
                    break;
            }
        } finally {
            mainLock.unlock();
        }
    }*/

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    public boolean isShutdown() {
        return ! isRunning(ctl.get());
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }
}