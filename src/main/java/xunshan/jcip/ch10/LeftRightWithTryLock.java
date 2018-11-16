package xunshan.jcip.ch10;

import xunshan.jcip.util.SimpleThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// 验证tryLock能否有效避免死锁
public class LeftRightWithTryLock {
    private ReentrantLock left = new ReentrantLock();
    private ReentrantLock right = new ReentrantLock();

    void leftRight() throws InterruptedException {
//        synchronized (left) {
//            SimpleThreadUtils.sleepSilently(1000);
//            synchronized (right) {
//
//            }
//        }
        while (true) {
            try {
                if (left.tryLock(1, TimeUnit.SECONDS)) {
                    System.out.println("left lock ok");
                    try {
                        if (right.tryLock(1, TimeUnit.SECONDS)) {
                            // do something
                            System.out.println("leftRight success");

                            right.unlock();

                            break;
                        } else {
                            System.out.println("leftRight fail to acquire right lock");
                            // left.unlock();
                            // sleep for a while to avoid lock conflicts
                            SimpleThreadUtils.sleepSilently(100);
                        }
                    } finally {
                        System.out.println("left lock released");
                        left.unlock();
                    }
                } else {
                    System.out.println("leftRight fail to acquire left lock");
                    // sleep for a while to avoid lock conflicts
                    SimpleThreadUtils.sleepSilently(100);
                }
            } finally {
                // left.unlock();
            }
        }
    }

    void rightLeft() {
        right.lock();
        try {
            SimpleThreadUtils.sleepSilently(3000);
            left.lock();
            try {
                System.out.println("rightLeft success");
            } finally {
                left.unlock();
            }
        } finally {
            right.unlock();
        }
    }
}
