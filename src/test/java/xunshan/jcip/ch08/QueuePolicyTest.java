package xunshan.jcip.ch08;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.*;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class QueuePolicyTest {
    @Mock
    RejectedExecutionHandler rejectedExecutionHandler;

    @Test(expected = RejectedExecutionException.class)
    public void synchronousQueue_RejectedWhenExceedMaxThreads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(15);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                10,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>());
        for (int i = 0; i < 15; i++) {
            executor.submit(() -> {
                SimpleThreadUtils.sleepWithoutInterrupt(1000);
                latch.countDown();
            });
        }
        latch.await();
    }

    @Test
    public void rejectedHandler() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        // queue bound is 5
        int bound = 3;
        ThreadPoolExecutor executor = getExecutor(bound);
        executor.setRejectedExecutionHandler(rejectedExecutionHandler);

        // submit 7 task which hang for 1 second
        // so only bound + max_threads will be submit successfully
        // another 2 threads will by rejected
        for (int i = 0; i < 7; i++) {
            executor.submit(() -> {
                SimpleThreadUtils.sleepWithoutInterrupt(1000);
                latch.countDown();
            });
        }

        latch.await();
        verify(rejectedExecutionHandler, times(2)).rejectedExecution(
                any(Runnable.class), any(ThreadPoolExecutor.class));
    }

    @Test
    public void discardOldestPolicy() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        // queue bound is 5
        int bound = 3;
        ThreadPoolExecutor executor = getExecutor(bound);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

        // submit 7 task which hang for 1 second
        // so only bound + max_threads will be submit successfully
        // another 2 **old** threads will by rejected
        for (int i = 0; i < 7; i++) {
            final int t = i;
            executor.submit(() -> {
                SimpleThreadUtils.sleepWithoutInterrupt(1000);
                System.out.println(t + " running");
                latch.countDown();
            });
        }

        latch.await();
    }

    @Test
    public void callerRunsPolicy() {
        int bound = 3;
        ThreadPoolExecutor executor = getExecutor(bound);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // two task will run in main thread
        for (int i = 0; i < 7; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName());
                SimpleThreadUtils.sleepWithoutInterrupt(1000);
            });
        }
        SimpleThreadUtils.sleepWithoutInterrupt(5000);
    }

    private ThreadPoolExecutor getExecutor(int bound) {
        return new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(bound));
    }
}
