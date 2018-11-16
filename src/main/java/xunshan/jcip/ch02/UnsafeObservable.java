package xunshan.jcip.ch02;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Observable;

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
        if (observable == null) {
            SimpleThreadUtils.sleepWithoutInterrupt(a);
            observable = new MyObservable();
        }
        return observable;
    }
}
