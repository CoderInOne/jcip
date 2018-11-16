package xunshan.jcip.base;

import xunshan.jcip.util.RandomUtil;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseProducer<E> implements Runnable {
    private int N_TRIALS;
    private CyclicBarrier barrier;
    private AtomicInteger putSum;
    private Consumer<E> c;
    private Function<Integer, E> seedValueMapper;

    public BaseProducer(int n_TRIALS,
                        CyclicBarrier barrier,
                        AtomicInteger putSum,
                        Consumer<E> c,
                        Function<Integer, E> seedValueMapper) {
        this.N_TRIALS = n_TRIALS;
        this.barrier = barrier;
        this.putSum = putSum;
        this.c = c;
        this.seedValueMapper = seedValueMapper;
    }

    @Override
    public void run() {
        try {
            int seed = (int) (this.hashCode() ^ System.nanoTime());
            int sum = 0;
            barrier.await();
            for (int i = 0; i < N_TRIALS; i++) {
                c.accept(seedValueMapper.apply(seed));
                sum = sum + seed;
                seed = RandomUtil.xorShift(seed);
            }
            putSum.getAndAdd(sum);
            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
