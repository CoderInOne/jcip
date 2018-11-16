package xunshan.jcip.forkjoin;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class CustomRecursiveTaskTest {

    @Test
    public void compute() {
        int len = 40;
        int[] array = IntStream.range(0, len).toArray();
        int sum = IntStream.range(0, len).filter(i -> i > 10 && i < 30).sum();

        final Integer res = new CustomRecursiveTask(array).compute();
        assertEquals(sum, res.intValue());
    }
}