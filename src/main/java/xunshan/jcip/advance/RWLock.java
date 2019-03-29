package xunshan.jcip.advance;

import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

// first reader-writer problem
public class RWLock {
    private final Sync sync = new Sync();
    private final Sync wsync = new Sync();
    private int readCount = 0; // visibility?

    /**
     * TODO learn JDK#tryAcquireShared(int), don't need to contend lock
     * each time
     */
    public void rlock() {
        sync.acquireShared(1);
        readCount++;
        if (readCount == 1) { //
            wsync.acquire(1);
        }
        sync.releaseShared(1);
    }

    public void rUnlock() {
        sync.acquireShared(1);
        readCount--;
        if (readCount == 0) {
            wsync.release(1);
        }
        sync.releaseShared(1);
    }

    public void wlock() {
        wsync.acquire(1);
    }

    public void wUnlock() {
        wsync.release(1);
    }

    @VisibleForTesting
    int getReadCount() {
        return this.readCount;
    }

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int acquires) {
            final Thread currentThread = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            }
            else if (currentThread == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("over flow");
                }
                setState(nextc); // no need CAS?
                return true;
            }
            return false;
        }

        /**
         * release lock, if state == 0 then setExclusiveOwnerThread(null)
         * @param release how many times release lock, always 1
         * @return true if free, at the time AQS#release will unpark successor
         *          or false
         */
        @Override
        protected boolean tryRelease(int release) {
            int c = getState() - release;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalStateException("caller should hold monitor");
            }
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }
    }
}
