package xunshan.jcip.ch14;

public class ThreadGate {
    private boolean isOpen;
    private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open() {
        generation++;
        isOpen = true;
        notifyAll();
    }

    // g1 {t1, t2, t3} await -> t4 open, generation = 1 -> {t1, t2} pass through
    // t10 close gate -> g2 {t20, t30, t40} await
    // t3 found isOpen = false, but arrivalGeneration = 0, so t3 pass through,
    // result is g1 all pass through after open(), and g2 all wait after close()
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation) {
            wait();
        }
    }
}
