package xunshan.jcip.base;

public class Timer implements Runnable {
    private boolean started = false;
    private long start, end = 0L;

    @Override
    public synchronized void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            start = t;
        } else {
            end = t;
        }
    }

    public synchronized long getTime() {
        return end - start;
    }
}