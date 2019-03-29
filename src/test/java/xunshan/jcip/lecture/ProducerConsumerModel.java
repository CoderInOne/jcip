package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 1 生产者生产的时候消费者不能消费
 * 2 消费者消费的时候生产者不能生产
 * 3 缓冲区空时消费者不能消费
 * 4 缓冲区满时生产者不能生产
 *
 * 作用：
 * 1 解耦。生产端和消费端各司其职，不需要互相关心
 * 2 协调生产端和消费端处理能力
 */
public class ProducerConsumerModel {
	private Queue<Integer> queue = new LinkedList<>();
	private final Object lock = new Object();
	private final int limit = 10;
	private int counter = 0;

	@Test
	public void test() throws InterruptedException {
		Thread t2 = new Thread(new Consumer());
		Thread t1 = new Thread(new Producer());

		t1.start(); t2.start();
		t1.join();  t2.join();
	}

	private class Producer implements Runnable {
		@Override
		public void run() {
			while (true) {
				// 先获取锁来塞数据
				synchronized (lock) {
					if (queue.size() == limit) {
						// 队列满了，等待
						lockWait();
					}

					queue.add(counter);
					System.out.println("producer " + Thread.currentThread().getName()
					          + " produced " + counter);
					counter += 1;

					if (queue.size() >= 1) {
						// 有数据了，通知别人
						lock.notify();
					}
				}

				SimpleThreadUtils.sleepSilently(100);
			}
		}
	}

	private class Consumer implements Runnable {
		@Override
		public void run() {
			while (true) {
				// 先获取锁来消费
				synchronized (lock) {
					if (queue.isEmpty()) {
						// 队列空，等下看看？
						lockWait();
					}

					Integer c = queue.remove();
					System.out.println("consumer " + Thread.currentThread().getName()
							+ " consumer " + c);

					if (queue.size() <= (limit - 1)) {
						lock.notify();
					}
				}

				SimpleThreadUtils.sleepSilently(1000);
			}
		}
	}

	private void lockWait() {
		try {
			lock.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
