package xunshan.jcip.ch05;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

public class FileCrawler implements Runnable {
    private final BlockingQueue<File> queue;
    private final Predicate<File> fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> queue, Predicate<File> fileFilter, File root) {
        this.queue = queue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void crawl(File root) throws InterruptedException {
        final File[] files = root.listFiles();
        if (files == null) {
            System.out.println("no file for " + root.getAbsolutePath());
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                crawl(f);
            }
            else {
                if (fileFilter.test(f)) {
                    queue.put(f);
                }
            }
        }
    }
}
