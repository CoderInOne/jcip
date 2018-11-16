package xunshan.jcip.cas;

import org.junit.Test;
import xunshan.jcip.base.ConsumerProducerTestEngine;

import java.util.concurrent.BrokenBarrierException;

import static org.junit.Assert.*;

public class ConcurrentStackTest {
    /*********** basic **************/
    @Test
    public void pushPop() {
        final ConcurrentStack<Integer> stack = new ConcurrentStack<>();
        stack.push(1);
        Integer i1 = stack.pop();
        assertEquals(1, i1.intValue());
    }

    /*********** non-block, so no blocking test ************/

    /*********** safety test **************/
    @Test
    public void testSafety() throws BrokenBarrierException, InterruptedException {
        final ConcurrentStack<Integer> stack = new ConcurrentStack<>();

        ConsumerProducerTestEngine<Integer> testEngine = new ConsumerProducerTestEngine.Param<Integer>()
                .setnThreads(10)
                .setnTrials(10000)
                .setConsumer(stack::push)
                .setSupplier(stack::pop)
                .setSeedValueMapper(seed -> seed)
                .setValueSeedMapper(seed -> seed)
                .build();

        boolean res = testEngine.start();
        assertTrue(res);

        testEngine.shutdown();
    }
}