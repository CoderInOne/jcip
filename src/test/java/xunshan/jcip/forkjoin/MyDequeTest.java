package xunshan.jcip.forkjoin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyDequeTest {

    private MyDeque deque;

    @Before
    public void setUp() throws Exception {
        deque = new MyDeque();
    }

    @Test
    public void getHeadIndex() {
        assertEquals(7, deque.getHeadIndex(0));
        assertEquals(6, deque.getHeadIndex(7));
    }

    @Test
    public void foo() {
        System.out.println(5 & 7);
        System.out.println(-3 & 7);
    }
}