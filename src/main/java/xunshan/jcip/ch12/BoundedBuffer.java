package xunshan.jcip.ch12;

import java.util.concurrent.Semaphore;

public class BoundedBuffer<E> {
    private E[] container;
    private Semaphore availableSemaphore;
    private Semaphore itemsSemaphore;
    private int putPosition = 0;
    private int takePosition = 0;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int bounded) {
        this.availableSemaphore = new Semaphore(bounded);
        this.itemsSemaphore = new Semaphore(0);
        this.container = (E[]) new Object[bounded];
    }

    public boolean isEmpty() {
        return itemsSemaphore.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSemaphore.availablePermits() == 0;
    }

    public void put(E e) throws InterruptedException {
        availableSemaphore.acquire();

        synchronized (this) {
            int i = putPosition;
            container[i] = e;
            putPosition = (++i == container.length) ? 0 : i;
        }

        itemsSemaphore.release();
    }

    public E take() throws InterruptedException {
        E e;
        itemsSemaphore.acquire();

        synchronized (this) {
            int i = takePosition;
            e = container[i];
            container[i] = null;
            takePosition = (++i == container.length) ? 0 : i;
        }

        availableSemaphore.release();
        return e;
    }
}
