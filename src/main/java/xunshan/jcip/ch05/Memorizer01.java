package xunshan.jcip.ch05;

import java.util.HashMap;
import java.util.function.Function;

public class Memorizer01<A, V> implements CacheLoader<A, V> {
    private Function<A, V> computable;
    private HashMap<A, V> cache = new HashMap<>();

    public Memorizer01(Function<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V load(A a) {
        V newValue = computable.apply(a);
        cache.put(a, newValue);
        return newValue;
    }

    @Override
    public synchronized V get(A a) {
        V result = cache.get(a);
        if (result == null) {
            return load(a);
        }
        else {
            return result;
        }
    }
}
