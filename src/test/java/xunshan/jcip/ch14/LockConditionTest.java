package xunshan.jcip.ch14;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest {
    private boolean open = false;
    @Test
    public void basic() {
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        SimpleThreadUtils.run(() -> {
            lock.lock();
            try {
                while (!open)
                    condition.await();

                System.out.println("out");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        lock.lock();
        try {
            SimpleThreadUtils.sleepSilently(1000);
            open = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
