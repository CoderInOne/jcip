package xunshan.jcip.ch05;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.math.BigInteger;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;

public class MemorizerTest {
    private Function<String, BigInteger> loader = s -> {
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        return new BigInteger(s);
    };

    @Test
    public void memorizer1() {
        test(new Memorizer01<>(loader));
    }

    @Test
    public void memorizer2() {
        test(new Memorizer02<>(loader));
    }

    @Test
    public void memorizer3() {
        test(new Memorizer03<>(loader));
    }

    public void test(CacheLoader<String, BigInteger> memorizer) {
        assertEquals(new BigInteger("10000"), memorizer.get("10000"));
        assertEquals(new BigInteger("100000"), memorizer.get("100000"));
    }
}