package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
	private final CountDownLatch latch = new CountDownLatch(3);

	@Test
	public void test() throws InterruptedException {
		new Thread(() -> {
			SimpleThreadUtils.sleepSilently(1000);
			latch.countDown();

			System.out.println("release one");
		}).start();

		new Thread(() -> {
			SimpleThreadUtils.sleepSilently(2000);
			latch.countDown();

			System.out.println("release two");
		}).start();

		new Thread(() -> {
			SimpleThreadUtils.sleepSilently(3000);
			latch.countDown();

			System.out.println("release three");
		}).start();

		latch.await();
	}
}
