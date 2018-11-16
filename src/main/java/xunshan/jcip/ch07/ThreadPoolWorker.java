package xunshan.jcip.ch07;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Typical thread-pool worker thread stucture
 */
public class ThreadPoolWorker extends Thread {
    /**
     * {@link ThreadPoolExecutor#runWorker(java.util.concurrent.ThreadPoolExecutor.Worker)}
     */
    @Override
    public void run() {
        Throwable t = null;
        Runnable task = getTaskFromWorkQueue(); // worker.firstTask
        try {
            while (task != null || (task = getTaskFromWorkQueue()) != null) {
                // lock for executing task
                // w.lock()

                // check interrupted and STOP

                try {
                    Throwable x;
                    try {
                        // beforeExecute(wt, task);
                        runTask(getTaskFromWorkQueue());
                    } catch (RuntimeException e) {
                        x = e; throw e;
                    } catch (Error er) {
                        x = er; throw er;
                    }
                    catch (Throwable th) {
                        x = th; throw new Error(th);
                    }
                    finally {
                        // afterExecute(task, thrown);
                    }
                } finally {
                    // w.unlock()
                }
            }
        } catch (Throwable e) {
            t = e;
        } finally {
            threadExited(this, t);
        }
    }

    /**
     * {@link ThreadPoolExecutor#processWorkerExit(java.util.concurrent.ThreadPoolExecutor.Worker, boolean)}
     * @param threadPoolWorker worker
     * @param t throwable
     */
    private void threadExited(ThreadPoolWorker threadPoolWorker, Throwable t) {

    }

    private void runTask(Runnable r) {
        r.run();
    }

    private Runnable getTaskFromWorkQueue() {
        return null;
    }


}
