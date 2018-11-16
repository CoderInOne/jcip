package xunshan.jcip.ch05;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

public class Memorizer03<A, V> implements CacheLoader<A, V> {
    private Function<A, V> computable;
    private Map<A, Future<V>> map = new ConcurrentHashMap<>();

    public Memorizer03(Function<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V load(final A a) {
        final FutureTask<V> future = new FutureTask<>(() -> computable.apply(a));

        // cache future
        map.put(a, future);

        future.run();
        return getValue(future);
    }

    private V getValue(Future<V> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public V get(A a) {
        Future<V> future = map.get(a);
        if (future == null) {
            return load(a);
        }
        return getValue(future);
    }
}
