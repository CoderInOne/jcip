package xunshan.jcip.ch14;

public class WaitBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public WaitBoundedBuffer(int capacity) {
        super(capacity);
    }

    @Override
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (isFull()) {
                    wait();   // can be notified by
                    continue; // reenter synchronized to get monitor
                }

                doPut(v);
                notifyAll();
                return;
            }
        }
    }

    @Override
    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (isEmpty()) {
                    wait();
                    continue;
                }

                V v = doTake();
                notifyAll();
                return v;
            }
        }
    }
}
