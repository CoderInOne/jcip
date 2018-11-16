package xunshan.jcip.ch15;

public class AtomicPseudomRandomTest extends PseudoRandomTest {
    @Override
    protected PseudoRandom getPseudoRandom() {
        return new AtomicPseudoRandom(100);
    }
}
