package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierTest {
    @Test
    public void test() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cb = new CyclicBarrier(3);
        SimpleThreadUtils.run(() -> {
            System.out.println("t1 before");
            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("t1 after");
        });

        SimpleThreadUtils.run(() -> {
            System.out.println("t2 before");
            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("t2 after");
        });

        // all is done
        cb.await();
    }
}
