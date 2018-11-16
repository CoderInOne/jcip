package xunshan.jcip.ch14;

public abstract class BaseBoundedBuffer<V> {
    protected final V[] buf;
    protected int tail;
    protected int head;
    protected int count;

    @SuppressWarnings("unchecked")
    public BaseBoundedBuffer(int capacity) {
        this.buf = (V[]) new Object[capacity];
        this.tail = 0;
        this.head = 0;
        this.count = 0;
    }

    public abstract void put(V v) throws InterruptedException;
    public abstract V take() throws InterruptedException;

    protected synchronized void doPut(V v) {
        buf[tail] = v;
        if (++tail == buf.length) {
            tail = 0;
        }
        count++;
    }

    protected synchronized V doTake() {
        V v = buf[head];
        buf[head] = null;
        if (++head == buf.length) {
            head = 0;
        }
        count--;
        return v;
    }

    // 插入之前用于判断是否已经满了
    public boolean isFull() {
        return count == buf.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int capacity() {
        return buf.length;
    }
}
