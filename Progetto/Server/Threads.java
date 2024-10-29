package Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {
    private static final int THREAD_POOL_SIZE = 10;
    private ExecutorService executorService;

    public Threads() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        Threads threadPool = new Threads();

        for (int i = 0; i < 20; i++) {
            threadPool.submitTask(new Task(i));
        }

        threadPool.shutdown();
    }
}

class Task implements Runnable {
    private int taskId;

    public Task(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Executing task " + taskId + " by " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // Simulate some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed by " + Thread.currentThread().getName());
    }
}