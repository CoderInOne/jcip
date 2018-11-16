package xunshan.jcip.ch05;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable {
    private final BlockingQueue<File> queue;
    private /*volatile*/ boolean running = true;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                File file = queue.take();
                indexFile(file);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void indexFile(File f) {
        System.out.println("indexing file:" + f.getAbsolutePath());
    }
}
