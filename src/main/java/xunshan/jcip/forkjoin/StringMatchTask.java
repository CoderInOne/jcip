package xunshan.jcip.forkjoin;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

// 5000000, threshold:10000, matchLen:10
// parallel: ~2.5s
// sequence: ~16s
public class StringMatchTask {
    private Map<String, Integer> map = new HashMap<>();
    private Map.Entry<String, Integer>[] entryArray;
    private final static char[] digit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private final static Random r = new Random();
    private List<String> toMatchList;
    private TaskConfig config;

    public static class TaskConfig {
        public int threshold = 10000;
        public int matchLen;
        public int srcSize;
    }

    public StringMatchTask(TaskConfig config, List<String> idList) {
        this.config = config;
        this.toMatchList = idList;
        generateData();
    }

    public void generateData() {
        for (int i = 0; i < config.srcSize; i++) {
            map.put(nextRandomString(config.matchLen), i);
        }
        entryArray = new Map.Entry[map.size()];
    }

    public List<Integer> start(boolean parallel) {
        if (parallel) {
            return new ComputeTask(0, entryArray.length - 1)
                    .compute();
        } else {
            return new ComputeTask(0, entryArray.length - 1)
                    .sequenceMatch(0, entryArray.length - 1);
        }
    }

    static String nextRandomString(int len) {
        StringBuilder sb = new StringBuilder();
        while (len-- >= 0) {
            sb.append(digit[r.nextInt(10)]);
        }
        return sb.toString();
    }

    private class ComputeTask extends RecursiveTask<List<Integer>> {
        private int start;
        private int end;

        public ComputeTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected List<Integer> compute() {
            if ((end - start) > config.threshold) {
                // TODO vs divide-into-same-size task
                return ForkJoinTask.invokeAll(createSubTask())
                        .parallelStream()
                        .map(ForkJoinTask::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            return sequenceMatch(start, end);
        }

        private List<ComputeTask> createSubTask() {
            List<ComputeTask> tasks = new ArrayList<>();
            tasks.add(new ComputeTask(start, start + (end - start) / 2));
            tasks.add(new ComputeTask(start + (end - start) / 2 + 1, end));
            return tasks;
        }

        List<Integer> sequenceMatch(int start, int end) {
            List<Integer> res = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                Map.Entry<String, Integer> e = entryArray[i];
                for (String s : toMatchList) {
                    if (e == null) {
                        continue;
                    }
                    if (e.getKey().endsWith(s)) {
                        res.add(e.getValue());
                    }
                }
            }
            return res;
        }
    }
}
