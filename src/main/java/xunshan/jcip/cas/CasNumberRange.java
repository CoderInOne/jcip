package xunshan.jcip.cas;

import java.util.concurrent.atomic.AtomicReference;

public class CasNumberRange {
    private static class IntPairs {
        final int lower;
        final int upper;

        public IntPairs(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPairs> values =
            new AtomicReference<>(new IntPairs(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public void setLower(int lower) {
        while (true) {
            IntPairs old = values.get();
            // check lower < upper

            if (values.compareAndSet(old, new IntPairs(lower, old.upper)))
                break;
        }
    }
}
