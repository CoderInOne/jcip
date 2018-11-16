package xunshan.jcip.ch02;

import xunshan.jcip.util.SimpleThreadUtils;

// Listing 2.8 in book
public class CachedFactorizer {
    private int lastNumber;
    private int[] lastFactors;
    private int hits;
    private int cachedHits;

    // param for testing only
    public long waitBeforeHitsInc = 0;

    // synchronized because maybe hits is being computed
    synchronized int hits() {
        return hits;
    }

    public int unsafeHits() {
        return hits;
    }

    synchronized int cachedHits() {
        return cachedHits;
    }

    public int[] compute(int i) {
        int[] factors = null;

        synchronized (this) {
            sleep(waitBeforeHitsInc);
            hits++;
            if (i == lastNumber) {
                cachedHits++;
                factors = lastFactors.clone();
            }
        }

        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }

        return factors;
    }

    // mock computing factors
    int[] factor(int i) {
        int[] res = new int[i];
        while (--i >= 0) {
            res[i] = i;
        }
        return res;
    }

    private void sleep(long t) {
        if (t > 0) {
            SimpleThreadUtils.sleepWithoutInterrupt(t);
        }
    }
}
