package xunshan.jcip.ch07;

import org.junit.Test;
import xunshan.jcip.util.SimpleThreadUtils;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

public class PrimeGeneratorTest {

    @Test(timeout = 2000)
    public void aSecondOfPrimes() {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        SimpleThreadUtils.sleepWithoutInterrupt(1000);
        generator.cancel();
    }
}