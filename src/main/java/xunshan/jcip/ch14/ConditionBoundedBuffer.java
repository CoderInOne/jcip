package xunshan.jcip.ch14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    private Lock lock = new ReentrantLock();
    private Condition nonFull = lock.newCondition();
    private Condition nonEmpty = lock.newCondition();

    public ConditionBoundedBuffer(int capacity) {
        super(capacity);
    }

    @Override
    public void put(V v) throws InterruptedException {
        lock.lock();
        try {
            while (count == buf.length)
                nonFull.await();

            buf[tail] = v;
            if (++tail == buf.length)
                tail = 0;
            count++;

            nonEmpty.signal();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public V take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                nonEmpty.await();

            V v = buf[head];
            buf[head] = null;
            if (++head == buf.length)
                head = 0;
            count--;

            nonFull.signal();
            return v;
        } finally {
            lock.unlock();
        }
    }
}
