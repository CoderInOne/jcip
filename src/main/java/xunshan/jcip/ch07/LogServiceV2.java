package xunshan.jcip.ch07;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// use high level thread pool to manage thread life cycle
public class LogServiceV2 {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void start() {}

    public void stop() {
        try {
            exec.shutdown();
            // block to termination
            exec.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String msg) {
        exec.execute(new WriteTask(msg));
    }

    private class WriteTask implements Runnable {
        private final String msg;

        private WriteTask(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            SimpleThreadUtils.sleepSilently(1000);
            System.out.println(msg);
        }
    }
}
