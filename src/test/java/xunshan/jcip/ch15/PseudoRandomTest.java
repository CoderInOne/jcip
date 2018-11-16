package xunshan.jcip.ch15;

import org.junit.Test;
import xunshan.jcip.base.Timer;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public abstract class PseudoRandomTest {

    @Test
    public void nextInt() {
        PseudoRandom pr = getPseudoRandom();

        for (int nThreads = 1; nThreads <= 64; nThreads *= 2) {
            final ExecutorService exec = Executors.newFixedThreadPool(nThreads);
            final Timer timer = new Timer();
            final CyclicBarrier barrier = new CyclicBarrier(nThreads + 1, timer);
            /**
             * 10000000 -> high contention, atomic slower a little bit than
             */
            final int trials = 1000;

            for (int j = 0; j < nThreads; j++) {
                exec.execute(() -> {
                    await(barrier);

                    for (int i = 1; i <= trials; i++) {
                        pr.nextInt(i);
                    }

                    await(barrier);
                });
            }

            await(barrier);
            await(barrier);

            System.out.println(this.getClass().getName() + "\t Threads:" + nThreads
                   + "\t Throughput: " + timer.getTime() / (nThreads * trials));
        }
    }

    private void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    protected abstract PseudoRandom getPseudoRandom();
}