package xunshan.jcip.ch05;

public interface CacheLoader<A, V> {
    V load(A a);
    V get(A a);
}
