package xunshan.jcip.lecture;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicTest {
	@Test
	public void threads() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				foo();
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				foo();
			}
		});

		t1.start(); t2.start();
	}


	private synchronized void foo() {
		// do something
	}

	@Test
	public void lock() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				bar();
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				bar();
			}
		});

		t1.start(); t2.start();
	}
	private Lock l = new ReentrantLock();
	private void bar() {
		l.lock();
		try {
			// do something
		}
		catch (Exception e) {
			// handle exception
		}
		finally {
			l.unlock();
		}
	}

	private final Object lockA = new Object();
	private Object lockB = new Object();
	@Test
	public void deadLock() throws InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				deadLockM1();
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				deadLockM2();
			}
		});

		t1.start(); t2.start();
		t1.join();  t2.join();
	}

	void deadLockM1() {
		synchronized (lockA) {
			SimpleThreadUtils.sleepSilently(100);
			synchronized (lockB) {
				// do something
			}
		}
	}

	void deadLockM2() {
		synchronized (lockB) {
			SimpleThreadUtils.sleepSilently(50);
//			synchronized (lockA) {
//				// do something
//			}
		}
	}
}
