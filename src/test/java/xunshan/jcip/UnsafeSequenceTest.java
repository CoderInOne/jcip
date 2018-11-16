package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.ch02.UnsafeSequence;
import xunshan.jcip.util.SimpleThreadUtils;

import static org.junit.Assert.*;

public class UnsafeSequenceTest {

    private UnsafeSequence unsafeSequence;

    @Before
    public void setUp() throws Exception {
        unsafeSequence = new UnsafeSequence();
    }

    @Test
    public void incSingleThread() {
        for (int i = 0; i < 10000; i++) {
            unsafeSequence.inc();
        }

        assertEquals(10000, unsafeSequence.value);
    }

    @Test
    public void incTwoThreads() {
        // t1
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.inc();
            }
            System.out.println("t1 finished");
        });

        // t2
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.inc();
            }
            System.out.println("t2 finished");
        });

        // wait two thread to finished
        SimpleThreadUtils.sleepWithoutInterrupt(5000);
        assertEquals(20000, unsafeSequence.value);
//        java.lang.AssertionError:
//        Expected :20000
//        Actual   :19251
    }

    @Test
    public void syncIncMultiThreads() {
        // t1
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.syncInc();
            }
            System.out.println("t1 finished");
        });

        // t2
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.syncInc();
            }
            System.out.println("t2 finished");
        });

        // t3
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.syncInc();
            }
            System.out.println("t3 finished");
        });

        // wait two thread to finished
        SimpleThreadUtils.sleepWithoutInterrupt(5000);
        assertEquals(30000, unsafeSequence.value);
    }

    @Test
    public void atomicInc() {
        // t1
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.atomicInc();
            }
            System.out.println("t1 finished");
        });

        // t2
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.atomicInc();
            }
            System.out.println("t2 finished");
        });

        // t3
        SimpleThreadUtils.run(() -> {
            for (int i = 0; i < 10000; i++) {
                unsafeSequence.atomicInc();
            }
            System.out.println("t3 finished");
        });

        // wait two thread to finished
        SimpleThreadUtils.sleepWithoutInterrupt(5000);
        assertEquals(30000, unsafeSequence.atomicValue.get());
    }
}