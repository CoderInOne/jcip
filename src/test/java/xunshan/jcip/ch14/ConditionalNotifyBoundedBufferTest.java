package xunshan.jcip.ch14;

import static org.junit.Assert.*;

public class ConditionalNotifyBoundedBufferTest extends BaseBufferTest {

    @Override
    protected BaseBoundedBuffer<Integer> getBufferImpl() {
        return new ConditionalNotifyBoundedBuffer<>(10);
    }
}