package xunshan.jcip;

import org.junit.Test;

import static org.junit.Assert.assertSame;

// think about object reference
// = for object means reference, 没有传递性
public class CloneTest {
    @Test
    public void cloneTest() {
        Object o0 = new Object();
        Object o = new Object();
        Object o1 = o;
        assertSame(o1, o);
        o = o0;
        assertSame(o1, o0);
    }
}
