package xunshan.jcip.thread;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadLocalVariableTest {
	private final ThreadLocal<String> tl = new ThreadLocal<>();

	@Test
	public void ds() throws InterruptedException {
		Thread t = new Thread(() -> {
			tl.set("hi");

			// do something

			System.out.println(tl.get());
		});

		t.start();
		t.join();
	}

	// common pattern
	@Test
	public void getAndSet() throws InterruptedException {
		ThreadLocal<String> tlStr = new ThreadLocal<>();
		final String str = "hi";

		// a new session
		Thread t = new Thread(() -> {
			try {
				// before request
				tlStr.set(str);

				// handle request
				assertEquals(str, tlStr.get());
			} finally {
				// clear context
				tlStr.remove();

				assertNull(tlStr.get());
			}
		});

		t.start();
		t.join();
	}
}