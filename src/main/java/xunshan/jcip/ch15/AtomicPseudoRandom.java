package xunshan.jcip.ch15;

import xunshan.jcip.util.RandomUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicPseudoRandom implements PseudoRandom {
    private AtomicInteger seed;

    public AtomicPseudoRandom(int seed) {
        this.seed = new AtomicInteger(seed);
    }

    @Override
    public int nextInt(int n) {
        while (true) {
            int s = seed.get();
            int next = RandomUtil.xorShift(s);
            if (seed.compareAndSet(s, next)) {
                int remainder = s % n;
                return remainder > 0 ? remainder : remainder + n;
            }
        }
    }
}
