package xunshan.jcip.forkjoin;

/**
 * {@link java.util.ArrayDeque}
 * key is changes of head and tail, and bitwise compute rule
 */
public class MyDeque {
    private int[] data = new int[8];
    private int head;
    private int tail;

    int getHeadIndex(int h) {
        return (h - 1) & (data.length - 1);
    }
}
