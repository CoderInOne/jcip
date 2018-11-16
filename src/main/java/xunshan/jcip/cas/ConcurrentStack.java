package xunshan.jcip.cas;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<E> {
    private final AtomicReference<Node<E>> head = new AtomicReference<>();

    public void push(E i) {
        Node<E> old;
        Node<E> newNode = new Node<>(i);
        do {
            old = head.get();
            newNode.next = old;
        } while (!head.compareAndSet(old, newNode));
    }

    public E pop() {
        Node<E> h;
        Node<E> next = null;
        do {
            h = head.get();
            if (h == null) {
                continue;
            }
            next = h.next;
        } while (h == null || !head.compareAndSet(h, next));

        return h.item;
    }

    final class Node<E> {
        final E item;
        Node<E> next;

        public Node(E item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + item +
                    ", next=" + next +
                    '}';
        }
    }
}
