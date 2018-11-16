package xunshan.jcip.basic;

/**
 * What if no reentrancy?
 * B#bar will be blocked forever.
 * Reentrancy is based on lock's reference count:
 * for example, calling B#bar which should hold lock b:
 *
 * enterMonitor                   [inside B#bar]
 *    refCount(lock b) = 1
 *    enterMonitor                [inside A#bar]
 *        refCount(lock b) = 2
 *    exitMonitor
 *        refCount(lock b) = 1
 * exitMonitor
 *    refCount(lock b) = 0        [lock released]
 */
public class Reentrancy {
    static class A {
        protected synchronized void foo() {

        }

        protected void bar() {
            synchronized (this) {
                // acquire lock automatically
                foo();
            }
        }
    }

    static class B extends A {
        @Override
        protected synchronized void foo() {
            super.foo();
        }

        @Override
        protected void bar() {
            synchronized (this) {
                // acquire lock
                super.bar();
            }
        }
    }

    public static void main(String[] args) {
        new B().bar();
    }
}
