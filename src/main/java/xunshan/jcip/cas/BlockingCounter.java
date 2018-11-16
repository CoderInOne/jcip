package xunshan.jcip.cas;

public class BlockingCounter {
    private int counter = 0;

    public synchronized void inc() {
        counter++;
    }

    public synchronized int get() {
        return counter;
    }
}
