package xunshan.jcip.basic;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Observable;

// Illustrate how a JDK Observable subclass miss thread safe initialization
public class UnsafeObservable {
    private static MyObservable observable;

    public static class MyObservable extends Observable {
        @Override
        protected synchronized void setChanged() {
            super.setChanged();
        }

        public void doChanged() {
            setChanged();
        }
    }

    public static MyObservable getInstance(long a) {
        // dangerous! if two thread do this check-and-act operation
        // race condition may happen, then one observable created fist and some observer
        // subscribe it. Unfortunately, another do it at very close time and create a new one
        // so subscribed observer before will be absent in new subscriber list
        if (observable == null) {
            SimpleThreadUtils.sleepWithoutInterrupt(a);
            observable = new MyObservable();
        }
        return observable;
    }
}
