package xunshan.jcip.ch02;

public class Reentrancy {
    static class A {
        protected synchronized void foo() {

        }

        protected void bar() {
            synchronized (this) {
                System.out.println(this);
                foo();
            }
        }
    }

    static class B extends A {
        @Override
        protected synchronized void foo() {
            // do something
            super.foo();
        }

        @Override
        protected void bar() {
            synchronized (this) {
                // do something
                System.out.println(this);
                super.bar();
            }
        }
    }

    public static void main(String[] args) {
        new B().bar();
    }
}
