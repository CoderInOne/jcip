package xunshan.jcip.ch07;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// better to use executor to manage queue and shutdown
public class LogService {
    private final BlockingQueue<String> queue;
    private final ExecutorService executorService;

    public LogService(BlockingQueue<String> queue) {
        this.queue = queue;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        executorService.submit(new LogTask());
    }

    public void log(String msg) throws InterruptedException {
        if (executorService.isShutdown()) {
            return;
        }

        queue.put(msg);
    }

    public void stop(long timeout) throws InterruptedException {
        System.out.println("stop");
        executorService.shutdown();
        executorService.awaitTermination(timeout, TimeUnit.SECONDS);
    }

    public boolean isShutdown() {
        return executorService.isTerminated() && queue.isEmpty();
    }

    private class LogTask implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        if (executorService.isShutdown() && queue.isEmpty()) {
                            System.out.println("looper: executor terminated");
                            break;
                        }
                        SimpleThreadUtils.sleepSilently(1000);

                        String msg = queue.take();
                        System.out.println(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {

            }
        }
    }
}
