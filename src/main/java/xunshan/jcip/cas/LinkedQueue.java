package xunshan.jcip.cas;

import java.util.concurrent.atomic.AtomicReference;

public class LinkedQueue<E> {
    private class Node<E> {
        private volatile E item;
        private volatile AtomicReference<Node<E>> next;

        Node(E item, Node<E> n) {
            this.item = item;
            this.next = new AtomicReference<>(n);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + item +
                    ", next=" + next +
                    '}';
        }
    }

    private final Node<E> dummy = new Node<>(null, null);
    private final AtomicReference<Node<E>> head =
            new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail =
            new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<>(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> residue = curTail.next.get();
            if (curTail == tail.get()) {
                if (residue == null) {                               /* A */
                    if (curTail.next.compareAndSet(null, newNode)) { /* C */
                        tail.compareAndSet(curTail, newNode);        /* D */
                        return true;
                    }
                } else {
                    tail.compareAndSet(curTail, residue);            /* B */
                }
            }
        }
    }

    public E get() {
        while (true) {
            // head
            Node<E> hn = this.head.get();

            // next
            Node<E> next = hn.next.get();
            if (next == null) {
                continue;
            }

            // WRONG: try update next of head, or retry
            // next.next.get() may be null, once head.next set null will lost
            // tracking tail, then no next added anymore
            // so if next.next.get() == null, then set tail as null too

            // CORRECT: great inspired by java.util.concurrent.ConcurrentLinkedQueue.poll
            E res = next.item;
            if (head.compareAndSet(hn, next)) {
                return res;
            }
        }
    }
}
