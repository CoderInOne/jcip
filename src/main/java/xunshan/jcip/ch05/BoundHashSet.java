package xunshan.jcip.ch05;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class BoundHashSet<T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        this.sem = new Semaphore(bound);
    }

    public boolean add(T e) throws InterruptedException {
        sem.acquire();
        boolean added = false;
        try {
            added = set.add(e);
            return added;
        }
        finally {
            if (!added) {
                sem.release();
            }
        }
    }

    public boolean remove(T e) {
        boolean removed = set.remove(e);
        if (removed) {
            sem.release();
        }
        return removed;
    }
}
