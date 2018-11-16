package xunshan.jcip.ch14;

public class ConditionalNotifyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public ConditionalNotifyBoundedBuffer(int capacity) {
        super(capacity);
    }

    @Override
    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean isEmpty = isEmpty();
        doPut(v);
        if (isEmpty)
            notifyAll();
    }

    @Override
    public synchronized V take() throws InterruptedException {
        while (isEmpty())
            wait();
        boolean isFull = isFull();
        V v = doTake();
        if (isFull)
            notifyAll();
        return v;
    }
}
