package xunshan.jcip.ch03;

import xunshan.jcip.util.SimpleThreadUtils;

public class Holder {
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n) {
            throw new AssertionError("fail!");
        }
    }

    public static void main(String[] args) {
        final Holder h = new Holder(10);
        SimpleThreadUtils.run(() -> {
            h.assertSanity();
        });
        SimpleThreadUtils.sleepWithoutInterrupt(100);
    }
}
