package xunshan.jcip.ch05;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memorizer02<A, V> implements CacheLoader<A, V> {
    private Function<A, V> computable;
    private Map<A, V> map = new ConcurrentHashMap<>();

    public Memorizer02(Function<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V load(A a) {
        V result = computable.apply(a);
        map.put(a, result);
        return result;
    }

    @Override
    public V get(A a) {
        final V result = map.get(a);
        if (result == null) {
            return load(a);
        }
        return result;
    }
}
