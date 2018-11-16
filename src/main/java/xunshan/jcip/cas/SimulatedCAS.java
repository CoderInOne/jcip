package xunshan.jcip.cas;

// This is a blocking simulation for CAS
public class SimulatedCAS {
    private int value;

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int old = value;
        if (old == expectedValue) {
            value = newValue;
        }
        return old;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
