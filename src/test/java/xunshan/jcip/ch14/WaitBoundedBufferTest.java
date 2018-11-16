package xunshan.jcip.ch14;

public class WaitBoundedBufferTest extends BaseBufferTest {
    @Override
    protected BaseBoundedBuffer<Integer> getBufferImpl() {
        return new WaitBoundedBuffer<>(3);
    }
}