package xunshan.jcip.stream;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public class AppTest {
    @Test
    public void parallelTraverseElements() {
        // engaged thread: {main, ForkJoinPool workers}
        IntStream.range(1, 100)
                .parallel()
                .forEach(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        System.out.println(Thread.currentThread().getName() + ": " + value);
                    }
                });
    }

    @Test
    public void parallelFilter() {
        // engaged thread: {main, ForkJoinPool workers}
        IntStream.range(1, 100)
                .parallel()
                .filter(new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        if (value % 2 == 0) {
                            System.out.println(Thread.currentThread().getName() + " filter: " + value);
                            return true;
                        }
                        return false;
                    }
                })
                .forEach(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        System.out.println(Thread.currentThread().getName() + " print: " + value);
                    }
                });
    }

    @Test
    public void splitApplyPhase() {
        // java.util.stream.ForEachOps.ForEachTask extends CountedCompleter
        IntStream.range(1, 100)
                .parallel()
                /** split phase
                 * {@link ForEachOps.ForEachTask#compute())} -> fork
                 * {@link Streams.RangeIntSpliterator#trySplit()} -> split policy
                 */
                .forEach(new IntConsumer() {
                    // apply phase
                    @Override
                    public void accept(int value) {
                        System.out.println(value);
                    }
                });
    }

    @Test
    public void combinePhase() {
        // java.util.stream.ForEachOps.ForEachTask extends CountedCompleter
        final List<String> list = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .parallel()
                .map(String::valueOf)
                .collect(Collectors.toList());
        // java.util.stream.AbstractTask.localResult doPut here after computation
        // java.util.stream.AbstractTask.compute
        // task.setLocalResult(task.doLeaf());
        // java.util.stream.ReduceOps.ReduceTask.onCompletion combine
        System.out.println(list);
    }

    @Test
    public void customerPool() throws ExecutionException, InterruptedException {
        final List<Long> list = LongStream.rangeClosed(1, 1000).boxed().collect(Collectors.toList());
        ForkJoinPool pool = new ForkJoinPool(4);
        final Long aLong = pool.submit(() -> list.stream().reduce(0L, Long::sum)).get();
        assertEquals(1001 * 1000 / 2, aLong.longValue());
    }
}