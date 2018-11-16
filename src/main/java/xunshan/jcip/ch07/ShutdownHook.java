package xunshan.jcip.ch07;

public class ShutdownHook {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("hook");
            }
        });
    }

    public static void main(String[] args) {

    }
}
