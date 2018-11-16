package xunshan.jcip.util;

import org.junit.Test;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class ThrowingConsumerTest {

    @Test(expected = RuntimeException.class)
    public void wrap() {
        Consumer<Integer> c = ThrowingConsumer.wrap(i -> throwIOException());
        c.accept(1);
    }

    private void throwIOException() throws IOException {
        throw new IOException();
    }
}