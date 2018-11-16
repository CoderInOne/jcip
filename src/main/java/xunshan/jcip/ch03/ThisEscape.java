package xunshan.jcip.ch03;

public class ThisEscape {
    private String unsafeValue;

    static class Event {
        String msg;
    }

    interface EventListener {
        void onEvent(Event e);
    }

    public ThisEscape(EventSource es) {
        // here ThisEscape.this is exposed to EventSource
        es.register(new EventListener() {
            @Override
            public void onEvent(Event e) {
                // can access this instance of enclosing instance
                unsafeValue.length();
                doSomething(e);
            }
        });
        this.unsafeValue = "hi";
    }

    // another place to modify private unsafe value
    private void doSomething(Event e) {

    }
}
