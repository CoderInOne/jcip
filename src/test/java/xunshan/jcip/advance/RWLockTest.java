package xunshan.jcip.advance;

import org.junit.Before;
import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public class RWLockTest {
    private RWLock rwLock;
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
    );

    private int c = 0;

    @Before
    public void setUp() throws Exception {
        this.rwLock = new RWLock();
    }

    /** basic **/
    @Test(expected = TimeoutException.class)
    public void writerCanNotAcquiredWLockBeforeRLockReleased()
            throws InterruptedException, ExecutionException, TimeoutException {
        rwLock.rlock();

        assertEquals(1, rwLock.getReadCount());

        // writer will fail
        Future<?> f = executor.submit(() -> {
            rwLock.wlock();
            fail(); // because read lock occupied access
        });

        f.get(1, TimeUnit.SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void writerShouldWaitMultiReaderReleased()
            throws InterruptedException, ExecutionException, TimeoutException {
        rwLock.rlock();
        rwLock.rUnlock();

        executor.submit(() -> {
            rwLock.rlock();
            SimpleThreadUtils.sleepSilently(2000);
            rwLock.rUnlock();
        });

        Future<?> f = executor.submit(() -> {
            rwLock.wlock();
            fail(); // should not get w lock
        });

        f.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void writerGotLock() {
        rwLock.rlock();
        assertEquals(0, c);
        rwLock.rUnlock();

        rwLock.wlock();
        c++;
        rwLock.wUnlock();

        assertEquals(1, c);
    }

    static int state = 0;
    static final int SHARED_SHIFT   = 16;
    static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
    static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
    static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;

    /** Returns the number of shared holds represented in count  */
    static int sharedCount(int c)    { return c >>> SHARED_SHIFT; }
    /** Returns the number of exclusive holds represented in count  */
    static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }

    @Test
    public void count() {
        state += 1;
        state += 1;
        state += 1;
        state += SHARED_UNIT;
        state += SHARED_UNIT;

        assertEquals(3, exclusiveCount(state));
    }
}






















