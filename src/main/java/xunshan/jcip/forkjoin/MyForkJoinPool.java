package xunshan.jcip.forkjoin;

import java.util.Deque;

public class MyForkJoinPool {
    public void submit(Runnable r) {
        // push into queue
    }

    public static class MyForkJoinTask  {
        private Deque<MyForkJoinTask>[] workQueues;
        private Deque<MyForkJoinTask> q;

        public Object compute() {
            return null;
        }
    }

    public static class MyForkJoinWorkerThread extends Thread {
        private MyForkJoinPool pool;
        private Deque<MyForkJoinTask> workQueue;

        @Override
        public void run() {
            // call pool.runWorker(workQueue);
        }
    }

    /**
     * 存在的一个问题：当本地没有任务可以执行，其他地方也偷不到任务的时候，等待的逻辑如何实现？
     */

    /*
    final void runWorker(ForkJoinPool.WorkQueue w) {
        w.growArray();                   // allocate queue
        int seed = w.hint;               // initially holds randomization hint
        int r = (seed == 0) ? 1 : seed;  // avoid 0 for xorShift
        for (ForkJoinTask<?> t;;) {
            if ((t = scan(w, r)) != null) // scan or steal
                w.runTask(t);
            else if (!awaitWork(w, r))    // wait or exit
                break;
            r ^= r << 13; r ^= r >>> 17; r ^= r << 5; // xorshift
        }
    }
    */
}
