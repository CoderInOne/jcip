package xunshan.jcip.ch14;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseBoundedBufferTest {
    private BaseBoundedBuffer<Integer> buffer;

    @Before
    public void setUp() throws Exception {
        buffer = new BaseBoundedBuffer<Integer>(3){
            @Override
            public void put(Integer integer) {
                doPut(integer);
            }

            @Override
            public Integer take() {
                return doTake();
            }
        };
    }

    @Test
    public void put() {
        buffer.doPut(1);
        assertEquals(1, buffer.doTake().intValue());

        buffer.doPut(2);
        buffer.doPut(3);
        buffer.doPut(4);
        assertTrue(buffer.isFull());
        
        assertEquals(2, buffer.doTake().intValue());
        assertEquals(3, buffer.doTake().intValue());
        assertEquals(4, buffer.doTake().intValue());
        assertTrue(buffer.isEmpty());
    }

    @Test
    public void isEmpty() {
        assertTrue(buffer.isEmpty());
    }

    @Test
    public void isFull() {
        buffer.doPut(1);
        buffer.doPut(2);
        buffer.doPut(3);
        assertTrue(buffer.isFull());
    }


}