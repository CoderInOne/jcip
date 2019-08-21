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
}