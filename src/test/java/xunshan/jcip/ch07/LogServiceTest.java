package xunshan.jcip.ch07;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class LogServiceTest {
    private LinkedBlockingQueue<String> queue;
    private LogService logService;

    @Before
    public void setUp() throws Exception {
        queue = new LinkedBlockingQueue<>(10);
        logService = new LogService(queue);
    }

    volatile boolean shutdownRequested = false;
    @Test
    public void shutdownSafely() throws InterruptedException {
        logService.start();

        SimpleThreadUtils.run(() -> {
            try {
                while (!shutdownRequested) {
                    SimpleThreadUtils.sleepWithoutInterrupt(200);
                    logService.log("hi");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        shutdownRequested = true;
        logService.stop(10);
        assertTrue(logService.isShutdown());
    }

    @Test
    public void logServiceByThreadPool() {
        final LogServiceV2 logServiceV2 = new LogServiceV2();
        for (int i = 0; i < 10; i++) {
            logServiceV2.log("hi");
        }
        System.out.println("begin to stop");
        logServiceV2.stop();
        System.out.println("after stop");
    }
}