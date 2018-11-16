package xunshan.jcip.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] arr;
    private final static int THRESHOLD = 20;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            // map-reduce 模型
            return ForkJoinTask.invokeAll(createSubTasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        }
        return processing(arr);
    }

    private Collection<CustomRecursiveTask> createSubTasks() {
        List<CustomRecursiveTask> res = new ArrayList<>();
        res.add(new CustomRecursiveTask(Arrays.copyOfRange(arr, 0, arr.length / 2)));
        res.add(new CustomRecursiveTask(Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return res;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
                .filter(i -> i > 10 && i < 30)
                .sum();
    }
}
