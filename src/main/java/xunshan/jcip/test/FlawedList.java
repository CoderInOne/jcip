package xunshan.jcip.test;

import java.util.ArrayList;

public class FlawedList<T> extends ArrayList<T> {
    public boolean putIfAbsent(T e) {
        boolean absent;
        synchronized (this) {
            absent = !super.contains(e);
            if (absent) {
                super.add(e);
            }
        }
        return absent;
    }
}
