package xunshan.jcip.ch15;

import xunshan.jcip.util.RandomUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockPseudoRandom implements PseudoRandom {
    private final Lock lock = new ReentrantLock();
    private int seed;

    public ReentrantLockPseudoRandom(int seed) {
        this.seed = seed;
    }

    @Override
    public int nextInt(int n) {
        lock.lock();
        try {
            int s = seed;
            seed = RandomUtil.xorShift(s);
            int remainder = s % n;
            return remainder > 0 ? remainder : remainder + n;
        }
        finally {
            lock.unlock();
        }
    }
}
