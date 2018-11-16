package xunshan.jcip.ch14;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import static org.junit.Assert.*;

public class ThreadGateTest {
    @Test
    public void basic() throws InterruptedException {
        final ThreadGate gate = new ThreadGate();
        SimpleThreadUtils.run(() -> {
            try {
                gate.await();
                System.out.println("open t1");
            } catch (InterruptedException e) {
                fail();
            }
        });
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepSilently(5000);
            gate.open();
        });

        gate.await();
        System.out.println("open main");
    }

    @Test
    public void close() {

    }
}