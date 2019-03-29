package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {
	private BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

	@Test
	public void test() throws InterruptedException {
		Thread t2 = new Thread(new BlockingQueueTest.Consumer());
		Thread t1 = new Thread(new BlockingQueueTest.Producer());

		t1.start(); t2.start();
		t1.join();  t2.join();
	}

	private class Producer implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Integer v = queue.take();
					System.out.println("consumer " + Thread.currentThread().getName()
					            + " consumed " + v);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}

				SimpleThreadUtils.sleepSilently(100);
			}
		}
	}

	private class Consumer implements Runnable {
		@Override
		public void run() {
			int c = 0;
			while (true) {
				try {
					queue.put(c);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}

				c += 1;

				SimpleThreadUtils.sleepSilently(2000);
			}
		}
	}
}
