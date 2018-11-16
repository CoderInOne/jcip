package xunshan.jcip.ch07;

import xunshan.jcip.util.SimpleThreadUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask) {
            return ((CancellableTask) callable).newTaskFor();
        }
        return super.newTaskFor(callable);
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8888);
        final CancellingExecutor cancellingExecutor =
                new CancellingExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        final Socket s = serverSocket.accept();

        // build socket task
        final SocketUsingTask<String> socketUsingTask = new SocketUsingTask<String>() {
            @Override
            public String call() throws Exception {
                SimpleThreadUtils.sleepWithoutInterrupt(10000);
                return "hi";
            }
        };
        socketUsingTask.setSocket(s);

        // get future, socket's close is encapsulated in future's cancel
        final RunnableFuture<String> future =
                cancellingExecutor.newTaskFor(socketUsingTask);

        // mock cancel action
        SimpleThreadUtils.run(() -> {
            SimpleThreadUtils.sleepWithoutInterrupt(5000);
            future.cancel(true);
        });

        // block get
        final String result = future.get();
    }
}
