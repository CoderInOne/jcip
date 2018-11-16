package xunshan.jcip;

import org.junit.Test;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.fail;
import static xunshan.jcip.util.SimpleThreadUtils.*;

public class FutureTaskTest {
    @Test
    public void basic() {
        final FutureTask<Integer> task = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return 1;
        });

        run(task);

        Thread t1 = run(() -> {
            try {
                System.out.println("start");

                task.get();

                System.out.println("end");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = run(() -> {
            try {
                task.get(200, TimeUnit.MILLISECONDS);
                fail();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("timeout");
            }
        });

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }
}
