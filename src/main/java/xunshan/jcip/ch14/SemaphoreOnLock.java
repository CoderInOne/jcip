package xunshan.jcip.ch14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreOnLock {
    private int permits;
    private final Lock lock = new ReentrantLock();
    private final Condition permitsAvailable = lock.newCondition();

    public SemaphoreOnLock(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        // lock
        //    while permits <= 0
        //       no condition wait
        //    permits--
        // unlock
        lock.lock();
        try {
            while (permits <= 0)
                permitsAvailable.await();
            permits--;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        // lock
        // permits++
        // signal permits changed
        // unlock
        lock.lock();
        try {
            permits++;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }
}
