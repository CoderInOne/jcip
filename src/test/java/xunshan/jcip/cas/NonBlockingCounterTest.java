package xunshan.jcip.cas;

import org.junit.Test;
import xunshan.jcip.base.Timer;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class NonBlockingCounterTest {

    @Test
    public void inc() throws BrokenBarrierException, InterruptedException {
        int N_THREAD = 2;
        int C_TIME = 1000000;
        Timer timer = new Timer();
        final CyclicBarrier barrier = new CyclicBarrier(N_THREAD + 1, timer);
        final BlockingCounter counter = new BlockingCounter();
//        final NonBlockingCounter counter = new NonBlockingCounter();

        for (int i = 0; i < N_THREAD; i++) {
            counting(barrier, counter::inc, C_TIME);
        }

        barrier.await();
        barrier.await();

        assertEquals(N_THREAD * C_TIME, counter.get());
        System.out.println(timer.getTime() / N_THREAD);

        /*
         * N_THREAD = 100, C_TIME = 100000
         * nonblock block
         * 13399472 4648309
         * 10529258 4176641
         * 11976264 4936655
         * 12801818 5434072
         * 12442250 5347659
         *
         * int N_THREAD = 1000;
         * int C_TIME = 1000000;
         *
         */
    }

    private void counting(CyclicBarrier barrier, Runnable r, int countTime) {
        SimpleThreadUtils.run(() -> {
            await(barrier);

            for (int i = 0; i < countTime; i++) {
                r.run();
            }
            await(barrier);
        });
    }

    private void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}