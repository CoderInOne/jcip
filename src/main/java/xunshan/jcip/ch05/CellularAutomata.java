package xunshan.jcip.ch05;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board mainBoard) {
        this.mainBoard = mainBoard;
        int count = Runtime.getRuntime().availableProcessors();
        System.out.println("cpu count:" + count);
        this.barrier = new CyclicBarrier(count, new Runnable() {
            @Override
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            this.workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    private class Worker implements Runnable {
        private Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            while (!board.hasConverged()) {
                System.out.println(Thread.currentThread().getName() +
                        " start " +
                        mainBoard.getMainCounter());

                while (!board.hasConverged()) {
                    board.compute();
                }

                System.out.println(Thread.currentThread().getName() +
                        " finished " +
                        mainBoard.getMainCounter());

                try {
                    barrier.await();

                    board.reset();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void start() {
        for (Worker worker : workers) {
            new Thread(worker).start();
        }
        mainBoard.waitForConvergence();
        System.out.println("main board finished");
    }

    public static void main(String[] args) {
        Board mainBoard = new Board(1000);
        final CellularAutomata cellularAutomata = new CellularAutomata(mainBoard);
        cellularAutomata.start();
    }
}
