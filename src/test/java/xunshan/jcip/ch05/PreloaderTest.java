package xunshan.jcip.ch05;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import static org.junit.Assert.*;

public class PreloaderTest {

    @Test
    public void get() throws InterruptedException, DataLoadException {
        final Preloader preloader = new Preloader();
        preloader.start();

        assertEquals("foo", preloader.get().getName());
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        assertEquals("foo", preloader.get().getName());

    }
}