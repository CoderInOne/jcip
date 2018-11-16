package xunshan.jcip.forkjoin;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StringMatchTaskTest {
    private final static char[] digit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private StringMatchTask stringMatchTask;
    private int matchLen = 10;

    @Before
    public void setUp() throws Exception {
        List<String> toMatchList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            toMatchList.add(StringMatchTask.nextRandomString(matchLen - 2));
        }
        StringMatchTask.TaskConfig config = new StringMatchTask.TaskConfig();
        config.threshold = 10000;
        config.srcSize = 5000_000;
        config.matchLen = 10;
        stringMatchTask = new StringMatchTask(config, toMatchList);
        stringMatchTask.generateData();
    }

    @Test
    public void sequence() {
        // ~8000ms
        long start = System.currentTimeMillis();
        final List<Integer> res = stringMatchTask.start(false);
        System.out.println("sequence:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void parallel() {
        long start = System.currentTimeMillis();
        final List<Integer> res = stringMatchTask.start(true);
        System.out.println("parallel:" + (System.currentTimeMillis() - start));

        assertArrayEquals(stringMatchTask.start(false).toArray(), res.toArray());
    }
}