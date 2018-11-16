package xunshan.jcip.base;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConsumerProducerTestEngine<E> {
    private ExecutorService exec;
    private CyclicBarrier barrier;
    private Param<E> param;

    private ConsumerProducerTestEngine(Param<E> param) {
        this.param = param;
    }

    public static class Param<E> {
        private int nThreads;
        private int nTrials;
        private Supplier<E> supplier;
        private Consumer<E> consumer;
        private Function<E, Integer> valueSeedMapper;
        private Function<Integer, E> seedValueMapper;
        private Consumer<Long> finishedConsumer;

        public Param<E> setnThreads(int nThreads) {
            this.nThreads = nThreads;
            return this;
        }

        public Param<E> setnTrials(int nTrials) {
            this.nTrials = nTrials;
            return this;
        }

        public Param<E> setSupplier(Supplier<E> supplier) {
            this.supplier = supplier;
            return this;
        }

        public Param<E> setConsumer(Consumer<E> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Param<E> setValueSeedMapper(Function<E, Integer> valueSeedMapper) {
            this.valueSeedMapper = valueSeedMapper;
            return this;
        }

        public Param<E> setSeedValueMapper(Function<Integer, E> seedValueMapper) {
            this.seedValueMapper = seedValueMapper;
            return this;
        }

        public Param<E> setFinishedConsumer(Consumer<Long> finishedConsumer) {
            this.finishedConsumer = finishedConsumer;
            return this;
        }

        public ConsumerProducerTestEngine<E> build() {
            return new ConsumerProducerTestEngine<>(this);
        }
    }

    public boolean start() throws BrokenBarrierException, InterruptedException {
        Timer timer = new Timer();
        barrier = new CyclicBarrier(2 * param.nThreads + 1, timer);
        exec = Executors.newFixedThreadPool(2 * param.nThreads);
        final AtomicInteger takeSum = new AtomicInteger();
        final AtomicInteger putSum  = new AtomicInteger();

        for (int i = 0; i < param.nThreads; i++) {
            exec.execute(new BaseProducer<>(param.nTrials, barrier, putSum, param.consumer, param.seedValueMapper));
            exec.execute(new BaseConsumer<>(param.nTrials, barrier, takeSum, param.supplier, param.valueSeedMapper));
        }

        barrier.await();
        barrier.await();

        if (param.finishedConsumer != null) {
            param.finishedConsumer.accept(timer.getTime());
        }

        return takeSum.get() == putSum.get();
    }

    public void shutdown() {
        if (exec != null) {
            exec.shutdownNow();
        }

        if (barrier != null) {
            barrier.reset();
        }
    }
}
