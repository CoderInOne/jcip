package xunshan.jcip.lecture;

import org.junit.Test;

public class RaceCond {
	private int counter = 0;

	@Test
	public void test() throws InterruptedException {
		int l = 100000;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < l; i++) {
					// fetch
					// add
					// write
					counter++;
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < l; i++) {
					counter++;
				}
			}
		});

		t1.start(); t2.start();
		t1.join();  t2.join();

		System.out.println("counter:" + counter);
	}
}
