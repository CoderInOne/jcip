package xunshan.jcip.ch03;

public class SafeListener {
    private String safeValue;
    private final ThisEscape.EventListener listener;

    private SafeListener() {
        this.listener = new ThisEscape.EventListener() {
            @Override
            public void onEvent(ThisEscape.Event e) {

            }
        };
    }

    public static SafeListener create(EventSource es) {
        SafeListener safeListener = new SafeListener();
        // construct completely then register
        es.register(safeListener.listener);
        return safeListener;
    }
}
