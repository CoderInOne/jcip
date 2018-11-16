package xunshan.jcip.ch07.exec;

/**
 * Code:ThreadPoolExecutorSourceTest
 * techniques:
 * 1. bitwise to store state and worker count, operators:[<<, ~, |, &]
 * 2. CAS to change state and worker count
 * 3. ReentrantLock: Lock held on access to workers set and related bookkeeping
 *    to avoid interrupt storm
 *    Condition: to support await termination
 *
 * design considerations:
 * 1. multiple state changes frequently and need speed -> bitwise, operation is fast enough
 * 2. state often changed by multiple threads and need concurrency -> no lock -> CAS
 * 3. ReentrantLock -> for threads only
 */
public class ThreadPoolExecutorSource {
}
