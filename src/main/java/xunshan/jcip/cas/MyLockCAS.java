package xunshan.jcip.cas;

import java.util.concurrent.atomic.AtomicBoolean;

// FIXME release code?
public class MyLockCAS {
    private AtomicBoolean lock = new AtomicBoolean();

    // if lock.get() == true, means others hold lock, then return
    // fast. if lock.get() == false, can access and content with
    // others safely
    public boolean lock() {
        return lock.compareAndSet(false, true);
    }
}
