package xunshan.jcip.ch07;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

// The program will be exit but p.nextProbablePrime may be blocked
// for a while.
public class PrimeGenerator implements Runnable {
    private final List<BigInteger> primes =
            new ArrayList<>();
    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> getPrimes() {
        return primes;
    }
}
