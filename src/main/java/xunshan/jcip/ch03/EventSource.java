package xunshan.jcip.ch03;

import java.util.ArrayList;
import java.util.List;

public class EventSource {
    private List<ThisEscape.EventListener> list = new ArrayList<>();

    void register(ThisEscape.EventListener l) {
        list.add(l);
        for (ThisEscape.EventListener el : list) {
            el.onEvent(new ThisEscape.Event());
        }
    }
}
