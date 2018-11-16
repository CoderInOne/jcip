package xunshan.jcip.ch14;

public class SleepBoundedBuffer<V> extends BaseBoundedBuffer<V>  {
    private long timeout = 1000;

    public SleepBoundedBuffer(int capacity) {
        super(capacity);
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }

            Thread.sleep(timeout);
        }
    }

    @Override
    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }

            Thread.sleep(timeout);
        }
    }
}
