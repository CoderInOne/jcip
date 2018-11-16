package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.base.Timer;
import xunshan.jcip.util.RandomUtil;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapPerfTest {
    private int bound = 10000;
    @Test
    public void test() {
        int t = 1000000;

        syncHashMap(t);
        SimpleThreadUtils.sleepSilently(1000);

        syncTreeMap(t);
        SimpleThreadUtils.sleepSilently(1000);

        concHashMap(t);
        SimpleThreadUtils.sleepSilently(1000);

        concSkipListMap(t);
        SimpleThreadUtils.sleepSilently(1000);
    }

    private void syncTreeMap(int t) {
        Map<Integer, Integer> map = Collections.synchronizedMap(new TreeMap<>());
        System.out.println("-------- TreeMap Trials: " + t + " ------------");
        for (int p = 1; p <= 128; p *= 2) {
            test(bound, p, t, map::put, map::get, map::remove);
        }
        System.out.println("\n");
    }

    private void concHashMap(int t) {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        System.out.println("-------- ConcurrentHashMap Trials: " + t + " ------------");
        for (int p = 1; p <= 128; p *= 2) {
            test(bound, p, t, map::put, map::get, map::remove);
        }
        System.out.println("\n");
    }

    private void concSkipListMap(int t) {
        Map<Integer, Integer> map = new ConcurrentSkipListMap<>();
        System.out.println("-------- ConcurrentSkipListMap Trials: " + t + " ------------");
        for (int p = 1; p <= 128; p *= 2) {
            test(bound, p, t, map::put, map::get, map::remove);
        }
        System.out.println("\n");
    }

    private void syncHashMap(int t) {
        Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());
        System.out.println("-------- HashMap Trials: " + t + " ------------");
        for (int p = 1; p <= 128; p *= 2) {
            test(bound, p, t, map::put, map::get, map::remove);
        }
        System.out.println("\n");
    }

    public void test(int bound, int pairs, final int nTrials,
                     final BiFunction<Integer, Integer, Integer> putAction,
                     final Function<Integer, Integer> getAction,
                     final Consumer<Integer> rmAction) {
        Timer timer = new Timer();
        CyclicBarrier barrier = new CyclicBarrier(pairs + 1, timer);
        ExecutorService exec = Executors.newFixedThreadPool(pairs);
        for (int i = 0; i < pairs; i++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    await(barrier);

                    int seed = (int) (this.hashCode() ^ System.nanoTime());
                    for (int j = 0; j < nTrials; j++) {
                        int key = seed % bound;
                        // get action
                        Integer res = getAction.apply(key);
                        if (res != null) {
                            // rm action
                            rmAction.accept(key);
                        }
                        else {
                            // doPut action
                            putAction.apply(key, key + 1);
                        }
                        seed = RandomUtil.xorShift(seed);
                    }

                    await(barrier);
                }
            });
        }

        await(barrier);
        await(barrier);

        System.out.println("Pairs: " + pairs + ", Throughput:" + timer.getTime() / (pairs * nTrials));

        exec.shutdownNow();
    }

    private void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
