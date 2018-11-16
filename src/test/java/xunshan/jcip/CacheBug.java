package xunshan.jcip;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import junit.framework.TestCase;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheBug extends TestCase {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    static final LoadingCache<String, Long> THE_CACHE = CacheBuilder.newBuilder()
            .refreshAfterWrite(1, TimeUnit.MILLISECONDS)
            .build(new CacheLoader<String, Long>() {
                @Override
                public Long load(String s) throws InterruptedException {
                    Thread.sleep(200);
                    return System.currentTimeMillis();
                }
            });

    public void testBug() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        final String key = "sa";
        THE_CACHE.getUnchecked(key);
        Thread.sleep(100);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    THE_CACHE.get(key);

                    Thread.sleep(10_000);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            THE_CACHE.invalidateAll();
            System.err.println("Current time is: " + THE_CACHE.get(key));
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start)); // ~1600,
        assertTrue(System.currentTimeMillis() - THE_CACHE.get(key) < 500);
    }
}
