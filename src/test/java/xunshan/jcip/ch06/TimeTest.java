package xunshan.jcip.ch06;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.util.Timer;
import java.util.TimerTask;

public class TimeTest {
    @Test
    public void basic() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("running");
            }
        }, 0, 1000);
        SimpleThreadUtils.sleepWithoutInterrupt(3500);
        timer.cancel();
    }

    @Test
    public void confusingTask() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        }, 1);
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        }, 1);
        SimpleThreadUtils.sleepWithoutInterrupt(2000);
    }
}
