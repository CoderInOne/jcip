package xunshan.jcip.ch15;

public class ReentrantLockPseudoRandomTest extends PseudoRandomTest {
    @Override
    protected PseudoRandom getPseudoRandom() {
        return new ReentrantLockPseudoRandom(100);
    }
}
