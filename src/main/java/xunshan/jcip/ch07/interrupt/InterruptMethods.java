package xunshan.jcip.ch07.interrupt;

import xunshan.jcip.util.SimpleThreadUtils;

public class InterruptMethods {
    private Object lock = new Object();

    void doWait() {
        synchronized (lock) {
            try {
                lock.wait();
                System.out.println("exit wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void doNotify() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public static void main(String[] args) {
        final InterruptMethods interruptMethods = new InterruptMethods();
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(100);
            interruptMethods.doNotify();
        });
        interruptMethods.doWait();
    }
}
