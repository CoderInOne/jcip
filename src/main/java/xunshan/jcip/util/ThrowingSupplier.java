package xunshan.jcip.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

    static <T> Supplier<T> wrap(ThrowingSupplier<T, Exception> s) {
        return () -> {
            try {
                return s.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
