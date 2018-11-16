package xunshan.jcip;

import org.junit.Test;
import xunshan.jcip.ch03.EventSource;
import xunshan.jcip.ch03.ThisEscape;

public class ThisEscapeTest {
    @Test(expected = NullPointerException.class)
    public void howThisExcapeByInnerClass() {
        new ThisEscape(new EventSource());
    }
}