# Code snippets of Java Concurrency in Practice

Every Java developer should read this book carefully! I'll give some brief code snippets that may be helpful
for understanding. **WARNING**: it's just a summary, dig in the book by yourself if you have doubts!

## Basic

### Thread-Unsafe & Race condition

```
int v = 0;

void update() {
    v++;
}
```

how `v` read and change?

- read `v` from memory
- add `1` by CPU
- write back into memory

race condition

```
two threads t1 and t2 change v at the same time
t1 ----->| read v = 0 |---->| v = v + 1 = 1 |---->| write v = 1|---> done
t2 --->| read v = 0 |---->| v = v + 1 = 1 |---->| write v = 1 |---> done
```

Solution? Need contract to limit threads' changing order.

### Atomicity: to do or not to do

Operation on `v` is atomic: each `update` method finished, `v` is increment by 1, or not

```
   t --->| read v = 0, v = v + 1 = 1, write v = 1 |---> done
or t --->| do nothing about v |-->
```

### Visibility

```
/*volatile*/ boolean flag = true;

@Test(timeout = 2000)
public void nonStop() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    new Thread(() -> {
        while (flag) { // 1
            // do nothing
        }
        latch.countDown();
    }).start();

    Thread.sleep(500);
    flag = false;

    latch.await();
}
```
Uncomment `volatile`, `line 1` always **see** newest `flag` and `flag` is visible.

### synchronized key word

```
int v = 0;

synchronized void update() {
    v++;
}
```

- synchronized is exclusive: only one thread can execute `update`
- guarantee both visibility and atomicity

### Locking

When `update` is locked, only one thread can hold it once; others should wait holder to realease

```
   t1 | read v = 0, v = v + 1 = 1, write v = 1 |---> done
                                            t2 | read v = 1, v = v + 1 = 2, write v = 2 |---> done
```

Question: after lock holder releases lock, other threads can acquire it, but who can and who control this
behavior?




