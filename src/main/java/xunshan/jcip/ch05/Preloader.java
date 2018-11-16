package xunshan.jcip.ch05;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static xunshan.jcip.util.SimpleThreadUtils.sleepWithoutInterrupt;

public class Preloader {
    private final FutureTask<ProductionInfo> future = new FutureTask<>(new Callable<ProductionInfo>() {
        @Override
        public ProductionInfo call() throws Exception {
            return loadProductInfo();
        }
    });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    private ProductionInfo loadProductInfo() throws DataLoadException {
        sleepWithoutInterrupt(1000);
        return new ProductionInfo("foo", 18);
    }

    public ProductionInfo get() throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            }
            else {
                throw launderThrowable(cause);
            }
        }
    }

    private RuntimeException launderThrowable(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        else if (cause instanceof Error) {
            throw (Error) cause;
        }
        else {
            throw new IllegalStateException("Not checked", cause);
        }
    }
}
