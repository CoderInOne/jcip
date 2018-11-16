package xunshan.jcip.util;

import java.util.function.Consumer;

/**
 * use to handle exception inside consumer, only for RuntimeException
 * https://www.baeldung.com/java-lambda-exceptions
 * @param <T>
 * @param <E>
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;

    static <T> Consumer<T> wrap(ThrowingConsumer<T, Exception> c) {
        return t -> {
            try {
                c.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
