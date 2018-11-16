package xunshan.jcip.ch14;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;

public class BoundedBufferPerfTest {
    @Test
    public void test1() throws BrokenBarrierException, InterruptedException {
        /*
         * ConditionalNotifyBoundedBuffer vs WaitBoundedBuffer
         */
        BaseBufferTest test1 = new BaseBufferTest() {
            @Override
            protected BaseBoundedBuffer<Integer> getBufferImpl() {
                return new ConditionalNotifyBoundedBuffer<Integer>(10);
            }
        };
        test1.safety();

//        BaseBufferTest test2 = new BaseBufferTest() {
//            @Override
//            protected BaseBoundedBuffer<Integer> getBufferImpl() {
//                return new WaitBoundedBuffer<>(10);
//            }
//        };
//        test2.safety();
    }
}
