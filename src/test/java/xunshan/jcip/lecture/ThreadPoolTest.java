package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

public class ThreadPoolTest {
	private ExecutorService pool = Executors.newFixedThreadPool(4);
	private int c = 10;

	@Test
	public void test() {
		for (int i = 0; i < 10; i++) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					c -= 1;
					System.out.println(c);
				}
			});
		}

		while (c != 0) {
			SimpleThreadUtils.sleepSilently(100);
		}

		System.out.println("finished");
	}

	/** simple map-reduce model implemented by thread pool */
	@Test
	public void mapReduce() throws ExecutionException, InterruptedException {
		BlockingQueue<Integer> list = new LinkedBlockingQueue<>();
		for (int i = 0; i < 1000_000; i++) {
			list.add(i);
		}

		List<Future<Integer>> taskList = new ArrayList<>();

		// map process
		for (int i = 0; i < 10; i++) {
			Future<Integer> future = pool.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					int s = 0;
					for (int j = 0; j < 10_000; j++) {
						s += 1;
					}
					return s;
				}
			});
			taskList.add(future);
		}

		// reduce process
		int sum = 0;
		for (Future<Integer> f : taskList) {
			Integer v = f.get();
			if (v != null) {
				sum += f.get();
			}
		}

		System.out.println("sum:" + sum);
	}
}
