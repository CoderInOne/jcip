package xunshan.jcip.ch12;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TestingThreadFactory implements ThreadFactory {
    final AtomicInteger threadCreated = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        threadCreated.getAndIncrement();
        return new Thread(r);
    }
}
