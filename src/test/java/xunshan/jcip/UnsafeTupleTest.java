package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.ch02.UnsafeTuple;
import xunshan.jcip.util.SimpleThreadUtils;

public class UnsafeTupleTest {

    @Test(expected = NullPointerException.class)
    public void update() {
        UnsafeTuple unsafeTuple = new UnsafeTuple();
        unsafeTuple.update(1, 2);
        SimpleThreadUtils.run(() -> {
            unsafeTuple.update(1, -1);
        });
        SimpleThreadUtils.sleepWithoutInterrupt(10);
        unsafeTuple.update(1, 2);
    }
}