package xunshan.jcip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import xunshan.jcip.ch02.UnsafeObservable;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Observer;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UnsafeObservableTest {
    @Mock
    Observer o1;
    @Mock
    Observer o2;

    @Test
    public void simpleObservation() {
        UnsafeObservable.MyObservable observable = new UnsafeObservable.MyObservable();
        observable.addObserver(o1);
        observable.addObserver(o2);
        observable.doChanged();
        observable.notifyObservers("hi");
        verify(o1).update(observable, "hi");
        verify(o2).update(observable, "hi");
    }

    // Lazy-init in multi thread env cause inconsistent behavior
    // Expected: Two observer share one observable and both notified when event fired up
    // Result: Only o1 notified. Reason is UnsafeObservable.getInstance(100) cause race condition
    @Test
    public void getInstance() {
        // observer1
        SimpleThreadUtils.run(() -> UnsafeObservable.getInstance(100).addObserver(o1));

        // observer2
        SimpleThreadUtils.run(() -> UnsafeObservable.getInstance(10).addObserver(o2));

        SimpleThreadUtils.sleepWithoutInterrupt(1000);

        UnsafeObservable.getInstance(0).doChanged();
        UnsafeObservable.getInstance(0).notifyObservers("hi");

        verify(o1).update(UnsafeObservable.getInstance(0), "hi");
        verify(o2, never()).update(UnsafeObservable.getInstance(0), "hi");
    }
}