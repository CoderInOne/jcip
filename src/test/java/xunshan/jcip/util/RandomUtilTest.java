package xunshan.jcip.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomUtilTest {

    @Test
    public void xorShift() {
        int seed = (int) (this.hashCode() ^ System.nanoTime());
        for (int i = 0; i < 10; i++) {
            System.out.println(seed % 1000);
            seed = RandomUtil.xorShift(seed);
        }
    }
}