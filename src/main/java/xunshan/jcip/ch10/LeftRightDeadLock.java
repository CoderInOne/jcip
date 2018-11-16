package xunshan.jcip.ch10;

import xunshan.jcip.util.SimpleThreadUtils;

public class LeftRightDeadLock {
    private Object left = new Object();
    private Object right = new Object();
    void leftRight() {
        synchronized (left) {
            SimpleThreadUtils.sleepSilently(1000);
            synchronized (right) {

            }
        }
    }

    void rightLeft() {
        synchronized (right) {
            SimpleThreadUtils.sleepSilently(1000);
            synchronized (left) {

            }
        }
    }
}
