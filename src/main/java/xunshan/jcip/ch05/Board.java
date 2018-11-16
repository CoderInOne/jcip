package xunshan.jcip.ch05;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;

// mock simulation by counting number
public class Board {
    private final static AtomicInteger mainCounter = new AtomicInteger(10);
    private final int n;
    private volatile int counter;
    private int index;

    public Board(int counter) {
        this.counter = counter;
        this.n = counter;
    }

    public void commitNewValues() {
        mainCounter.decrementAndGet();
    }

    public void waitForConvergence() {
        while (mainCounter.get() != 0) {
            SimpleThreadUtils.sleepWithoutInterrupt(10);
        }
    }

    public Board getSubBoard(int nThreads, int index) {
        this.index = index;
        return new Board(nThreads * (index + 1) * 1000);
    }

    public void compute() {
        SimpleThreadUtils.sleepWithoutInterrupt(index + 1);
        counter--;
    }

    public void reset() {
        this.counter = n;
    }

    public boolean hasConverged() {
        return counter == 0 || mainCounter.get() == 0;
    }

    public int getMainCounter() {
        return mainCounter.get();
    }
}
