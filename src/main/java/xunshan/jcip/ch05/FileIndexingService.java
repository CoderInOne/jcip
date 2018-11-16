package xunshan.jcip.ch05;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

// How Consumer & Producer can cooperate?
// When FileCrawler is traverse all files, but Indexer still
// try to doTake files from queue
public class FileIndexingService {
    private static final int BOUND = 1024;
    private static final int N_CONSUMERS = 16;

    public static void main(String[] args) {
        final BlockingQueue<File> queue = new ArrayBlockingQueue<>(BOUND);
        Predicate<File> fileFilter = f -> true;
        File root = new File("/Users/mozat/loops-server/");
        File[] files = root.listFiles();

        for (File f : files) {
            new Thread(new FileCrawler(queue, fileFilter, f)).start();
        }

        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}
