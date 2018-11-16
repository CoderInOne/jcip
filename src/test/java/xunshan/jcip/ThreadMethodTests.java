package xunshan.jcip;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class ThreadMethodTests {
    @Test
    public void alive() {
        Thread.currentThread().interrupt();
        // TODO does run method exit mean thread is not alive?
        assertTrue(Thread.currentThread().isAlive());
    }
}
