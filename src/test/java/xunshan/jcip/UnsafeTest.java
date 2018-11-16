package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;
import xunshan.jcip.util.SimpleThreadUtils;

public class UnsafeTest {
    Unsafe UNSAFE;
    private long parkBlockerOffset;

    @Before
    public void setUp() throws Exception {
        UNSAFE = sun.misc.Unsafe.getUnsafe();
        Class<?> tk = Thread.class;
        parkBlockerOffset = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("parkBlocker"));
    }

    /**
     * java.lang.SecurityException: Unsafe
     *
     * 	at sun.misc.Unsafe.getUnsafe(Unsafe.java:90)
     */
    @Test
    public void park() {
        final Thread t = SimpleThreadUtils.run(() -> {
            UNSAFE.putObject(Thread.currentThread(), parkBlockerOffset, UnsafeTest.this);
            UNSAFE.park(false, 0L);
            System.out.println("unpark");
            UNSAFE.putObject(Thread.currentThread(), parkBlockerOffset, null);
        });

        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            UNSAFE.unpark(t);
        });

        SimpleThreadUtils.sleepSilently(3000);
    }
}
