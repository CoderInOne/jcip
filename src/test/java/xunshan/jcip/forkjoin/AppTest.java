package xunshan.jcip.forkjoin;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void basic() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(3);
        final ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            latch.countDown();
        });
        pool.execute(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            latch.countDown();
        });
        pool.execute(() -> {
            SimpleThreadUtils.sleepSilently(1000);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void forkJoinTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        final BaseForkJoinTask<Void> task1 = new BaseForkJoinTask<Void>() {
            @Override
            protected boolean exec() {
                run(latch);
                return false;
            }
        };
        task1.fork();
        final BaseForkJoinTask<Void> task2 = new BaseForkJoinTask<Void>() {
            @Override
            protected boolean exec() {
                run(latch);
                return false;
            }
        };
        task2.fork();
        final BaseForkJoinTask<Void> task3 = new BaseForkJoinTask<Void>() {
            @Override
            protected boolean exec() {
                run(latch);
                return false;
            }
        };
        task3.fork();
        latch.await();
    }

    private void run(CountDownLatch latch) {
        SimpleThreadUtils.sleepSilently(1000);
        latch.countDown();
        System.out.println(Thread.currentThread().getName() + " running");
    }

    private abstract class BaseForkJoinTask<T> extends ForkJoinTask<T> {
        @Override
        public T getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(T value) {

        }
    }
}