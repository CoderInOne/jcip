package xunshan.jcip.cas;

// non-thread-safe lock
public class MyLock {
    private boolean locked = false;

    // synchronized make it thread safe, but only one thread can
    // check-and-act, maybe fail to get lock, but still wait
    public /*synchronized*/ boolean lock() {
        if (!locked) {
            locked = true;
            return true;
        }

        return false;
    }
}
