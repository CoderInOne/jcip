package xunshan.jcip.ch14;

public class SleepBoundedBufferTest extends BaseBufferTest {
    @Override
    protected BaseBoundedBuffer<Integer> getBufferImpl() {
        SleepBoundedBuffer<Integer> buffer = new SleepBoundedBuffer<>(10);
        buffer.setTimeout(10);
        return buffer;
    }
}