package xunshan.jcip.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class NonBlockingCounter {
    private final AtomicInteger counter = new AtomicInteger(0);

    public void inc() {
        int v;
        do {
            v = counter.get();
        } while (!counter.compareAndSet(v, v + 1));
    }

    public int get() {
        return counter.get();
    }
}
