package xunshan.jcip;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.ch02.CachedFactorizer;
import xunshan.jcip.util.SimpleThreadUtils;

import static org.junit.Assert.*;

public class CachedFactorizerTest {

    private CachedFactorizer factorizer;

    @Before
    public void setUp() throws Exception {
        factorizer = new CachedFactorizer();
    }

    @Test
    public void hits() {
        fail();
    }

    // bad test
    @Test
    public void unsafeHits() {
        factorizer.waitBeforeHitsInc = 100;
        SimpleThreadUtils.run(() -> {
            factorizer.compute(2);
        });
        SimpleThreadUtils.run(() -> {
            factorizer.compute(2);
        });
        SimpleThreadUtils.sleepWithoutInterrupt(300);
        assertEquals(1, factorizer.unsafeHits());
    }

    @Test
    public void cachedHits() {
        fail();
    }

    @Test
    public void compute() {
        fail();
    }

    @Test
    public void factor() {
        fail();
    }
}