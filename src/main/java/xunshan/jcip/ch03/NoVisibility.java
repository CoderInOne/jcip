package xunshan.jcip.ch03;

public class NoVisibility extends Thread {
boolean keepRunning = true;
// using volatile means ready always read from heap, not register
/*volatile*/ boolean ready = false;
public static void main(String[] args) throws InterruptedException {
    NoVisibility t = new NoVisibility();
    t.start();
    Thread.sleep(100);
    t.keepRunning = false;
    t.ready = true;
    System.out.println(System.currentTimeMillis() + ": keepRunning is false");
}
public void run() {
    while (!ready)
    {
    }
}
}
