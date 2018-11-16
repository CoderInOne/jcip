package xunshan.jcip.ch07;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class LogWriterTest {

    @Test
    public void put() {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        final LogWriter logWriter = new LogWriter(queue);
        logWriter.start();

        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(3000);
            logWriter.stop();
        });

        try {
            while (true) {
                SimpleThreadUtils.sleepWithoutInterrupt(100);
                logWriter.put("hi");
            }
        } catch (InterruptedException | IllegalStateException e) {
            System.err.println("interrupt producer");
        }
    }
}