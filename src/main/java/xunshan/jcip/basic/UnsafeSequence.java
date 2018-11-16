package xunshan.jcip.basic;

import java.util.concurrent.atomic.AtomicInteger;

public class UnsafeSequence {
    public int value;
    public AtomicInteger atomicValue;

    public UnsafeSequence() {
        this.atomicValue = new AtomicInteger(0);
    }

    public void inc() {
        this.value++;
    }

    public void atomicInc() {
        this.atomicValue.incrementAndGet();
    }

    public synchronized void syncInc() {
        this.value++;
    }
}
