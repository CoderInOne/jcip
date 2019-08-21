package xunshan.jcip.thread;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadStateTest {
	@Test
	public void newState() {
		Thread thread = new Thread();
		System.out.println(thread.getState());
	}

	@Test
	public void runnableState() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getState());
			}
		});
		thread.start();
		thread.join();
	}

	private int counter = 0;


	@Test
	public void waitingState() throws InterruptedException {
		Lock lock = new ReentrantLock();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TIMED_WAITING
				SimpleThreadUtils.sleepSilently(1000);

				// WAITING
				lock.lock();

				lock.unlock();
			}
		});

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();

				while (counter++ < 20) {
					SimpleThreadUtils.sleepSilently(200);
					System.out.println(thread.getState());
				}

				lock.unlock();
			}
		});

		thread.start(); thread1.start();
		thread.join();  thread1.join();
	}

	private final Object lock = new Object();

	@Test
	public void blockedState() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TIMED_WAITING
				SimpleThreadUtils.sleepSilently(1000);

				// BLOCKED
				synchronized (lock) {

				}
			}
		});

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					while (counter++ < 20) {
						SimpleThreadUtils.sleepSilently(200);
						System.out.println(thread.getState());
					}
				}
			}
		});

		thread.start(); thread1.start();
		thread.join();  thread1.join();
	}

	@Test
	public void terminatedState() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

			}
		});

		t.start();
		t.join();

		System.out.println(t.getState());
	}
}