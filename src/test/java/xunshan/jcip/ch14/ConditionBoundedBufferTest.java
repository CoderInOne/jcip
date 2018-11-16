package xunshan.jcip.ch14;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ConditionBoundedBufferTest extends BaseBufferTest {

    @Override
    protected BaseBoundedBuffer<Integer> getBufferImpl() {
        return new ConditionBoundedBuffer<>(10);
    }

    @Test(timeout = 1000)
    public void multiWait() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            SimpleThreadUtils.run(() -> {
                try {
                    buffer.take();
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        buffer.put(3);
        buffer.put(3);
        buffer.put(3);
        buffer.put(3);
        buffer.put(3);

        latch.await();
    }
}