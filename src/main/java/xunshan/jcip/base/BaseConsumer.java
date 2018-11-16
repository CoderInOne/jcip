package xunshan.jcip.base;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseConsumer<E> implements Runnable {
    private int N_TRIALS;
    private CyclicBarrier barrier;
    private AtomicInteger takeSum;
    private Supplier<E> supplier;
    private Function<E, Integer> valueSeedMapper;

    public BaseConsumer(int n_TRIALS,
                        CyclicBarrier barrier,
                        AtomicInteger takeSum,
                        Supplier<E> supplier,
                        Function<E, Integer> valueSeedMapper) {
        N_TRIALS = n_TRIALS;
        this.barrier = barrier;
        this.takeSum = takeSum;
        this.supplier = supplier;
        this.valueSeedMapper = valueSeedMapper;
    }

    @Override
    public void run() {
        try {
            barrier.await();
            int sum = 0;
            for (int i = 0; i < N_TRIALS; i++) {
                E v = supplier.get();
                assert v != null;
                sum += valueSeedMapper.apply(v);
            }
            takeSum.getAndAdd(sum);
            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
