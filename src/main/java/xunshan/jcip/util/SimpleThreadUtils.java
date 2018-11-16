package xunshan.jcip.util;

public class SimpleThreadUtils {
    public static Thread run(Runnable r) {
        Thread t = new Thread(r);
        t.start();
        return t;
    }

    public static void sleepWithoutInterrupt(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSilently(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {

        }
    }
}
