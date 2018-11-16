package xunshan.jcip.ch02;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Illustrate not thread-safe operation to two thread-safe object
 */
public class UnsafeTuple {
    AtomicInteger var1;
    AtomicLong var2;

    public void update(int k, long v) {
        // init
        if (var1 == null) {
            var1 = new AtomicInteger(k);
            var2 = new AtomicLong(v);
        }
        // reset
        else if (v == -1L) {
            var2 = null;
            SimpleThreadUtils.sleepWithoutInterrupt(100);
            var1 = null;
        }
        // update
        else {
            // this line maybe cause NPE, because line 21~23, is not atomic
            // when one thread executing line 22, while other thread update again
            // this time var1 still not null, so line 30 will be executed, then var2 is null!
            var2.compareAndSet(var2.get(), v);
        }
    }
}
