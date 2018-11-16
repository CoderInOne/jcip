package xunshan.jcip.ch07;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

// encapsulate cancel with future
public interface CancellableTask<T> extends Callable<T> {
    void cancel();
    RunnableFuture<T> newTaskFor();
}
