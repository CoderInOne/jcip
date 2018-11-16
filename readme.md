# Code snippets of Java Concurrency in Practice

Java Concurrency in Practice is a great book. Here are what I learnt:

- basic concepts of concurrency and parallelism, like thread safe, race condition, visibility, lock
- how to keep code correct and safe when writing concurrent code
- how to design concurrent logic, like check-and-act against concurrent issue, blocking queue
- how to improve performance of concurrent code
- advanced topic: CAS, AQS, Lock API

Here, I give some brief code snippets that help me understand when I read it. Besides, I give some
references out of the book when I need it, like `Object#wait`.

**WARNING**: it's just a summary, dig in the book by yourself if you have doubts!

Pull requests are welcome!

## Basic

### Thread-Unsafe & Race condition

```
int v = 0;

void update() {
    v++;
}
```

How `v` read and change?

- read `v` from memory
- increment `v` by CPU
- write back into memory

Race condition

```
two threads t1 and t2 change v at the same time
t1 ----->| read v = 0 |---->| v = v + 1 = 1 |---->| write v = 1|---> done
t2 --->| read v = 0 |---->| v = v + 1 = 1 |---->| write v = 1 |---> done
```

Example:

- [UnsafeSequence](src/main/java/xunshan/jcip/basic/UnsafeSequence.java)
- [UnsafeObservable](src/main/java/xunshan/jcip/basic/UnsafeObservable.java)
- [UnsafeTuple](src/main/java/xunshan/jcip/basic/UnsafeTuple.java)

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

See [NoVisibilityTest](src/test/java/xunshan/jcip/NoVisibilityTest.java)

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

### How to share objects safely

- Thread-confined
  - stack confinement: shared variable used and updated only in method and no escape
  - JDBC pool or SpringMVC: one request, one thread
  - ThreadLocal: every thread has its own variable that is invisible for others
- Immutable objects: no value can be changed
- Share thread-safe
- Guarded

### Blocking

#### Object#wait & #notify

[Threads in Java: Live Example: sleep, yield, wait, notify; inter-thread communications](https://www.youtube.com/watch?v=1BvYJMgIAeU)
is a great live example to illustrate sleep, yield, wait and notify topic. Really interesting!

See [ObjectWaitTest](src/test/java/xunshan/jcip/ObjectWaitTest.java) 

#### Blocking data structure

- BlockingQueue
- ConcurrentHashMap
- Deque and work stealing

#### Producer-consumer pattern

#### Blocking & interrupt

#### ConcurrentModificationException

### Synchronizer

- CountDownLatch
- FutureTask
- Semaphore
- Barriers

### HashTable, synchronizedMap, ConcurrentHashMap and cache