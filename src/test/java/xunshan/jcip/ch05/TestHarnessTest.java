package xunshan.jcip.ch05;

import org.junit.Test;

import static xunshan.jcip.util.SimpleThreadUtils.sleepWithoutInterrupt;

public class TestHarnessTest {

    @Test
    public void taskTime() throws InterruptedException {
        TestHarness.taskTime(() -> sleepWithoutInterrupt(1000), 5);
    }
}