package xunshan.jcip.ch07;

import java.util.concurrent.*;

public class TimedRun {
    private final ScheduledExecutorService schedExec =
            Executors.newScheduledThreadPool(2);
    private final ExecutorService exec =
            Executors.newFixedThreadPool(2);

    // without catching InterruptedExcepting timed run
    public void timedRun(Runnable r, long timeout, TimeUnit timeUnit) {
        final Thread taskThread = Thread.currentThread();

        // schedule taskThread.interrupt() as timeout mechanism
        schedExec.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        taskThread.interrupt();
                    }
                },
                timeout, timeUnit);

        // run task, when taskThread.interrupt() call, any operation
        // in thread catching InterruptedException will throw it
        r.run();
    }

    public void timedRunV2(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowTask implements Runnable {
            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    r.run();
                } catch (RuntimeException t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null) {
                    System.err.println("rethrow:" + t.getMessage());
                    throw new RuntimeException(t);
                }
            }
        }

        RethrowTask rethrowTask = new RethrowTask();
        Thread taskThread = new Thread(rethrowTask);
        taskThread.start();
        schedExec.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        taskThread.interrupt();
                    }
                },
                timeout,
                unit);
        // it will be too soon to pass so that
        // rethrow will not get throwable
        // taskThread.join(unit.toMillis(timeout));
        taskThread.join();
        rethrowTask.rethrow();
    }

    public void timedRunV3(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        final Future<?> future = exec.submit(r);
        try {
            future.get(timeout, unit);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // cancel will cause sleep in r throw InterruptedException
            future.cancel(true);
        }
    }

    public static void main(String[] args) {
        final TimedRun timedRun = new TimedRun();
        try {
            timedRun.timedRunV3(new Runnable() {
                @Override
                public void run() {
                    // throw InterruptedException, but can not catch
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.err.println("task run exception:" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            // catch wrapped RuntimeException
            e.printStackTrace();
        }
        System.out.println("exit");
        timedRun.exec.shutdown();
    }
}
