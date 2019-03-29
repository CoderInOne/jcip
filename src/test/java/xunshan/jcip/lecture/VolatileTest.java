package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

public class VolatileTest {
	private volatile int flag = 0;

	@Test
	public void test() throws InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (flag == 0) {
					// do nothing
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				SimpleThreadUtils.sleepSilently(5000);
				flag = 1;
				System.out.println("t2 out");
			}
		});

		t1.start(); t2.start();
		t1.join();  t2.join();
	}
}
