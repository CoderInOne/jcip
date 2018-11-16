package xunshan.jcip.test;

import com.google.testing.threadtester.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlawedListTest {
    // test without concurrent check
    @Test
    public void junit() {
        FlawedList<Integer> list = new FlawedList<>();
        list.putIfAbsent(1);
        list.putIfAbsent(1);
        assertEquals(1, list.size());
    }

    private FlawedList<Integer> flawedList;

    @ThreadedBefore
    public void setUp() {
        flawedList = new FlawedList<>();
    }

    @ThreadedMain
    public void main() {
        flawedList.putIfAbsent(1);
    }

    @ThreadedSecondary
    public void second() {
        flawedList.putIfAbsent(1);
    }

    @ThreadedAfter
    public void after() {
        assertEquals(1, flawedList.size());
    }

    @Test
    public void testFlawedList() {
        AnnotatedTestRunner runner = new AnnotatedTestRunner();
        runner.runTests(getClass(), FlawedList.class);
    }
}