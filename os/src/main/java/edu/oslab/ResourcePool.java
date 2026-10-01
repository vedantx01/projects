package edu.oslab;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public final class ResourcePool {
    private ResourcePool() { }

    public static void runDemo() throws InterruptedException {
        System.out.println("Process pool (bounded workers):");
        AtomicInteger processWorker = new AtomicInteger();
        ThreadFactory processFactory = task -> {
            Thread thread = new Thread(task, "process-worker-" + processWorker.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
        runPool(new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(4), processFactory, new ThreadPoolExecutor.CallerRunsPolicy()),
                "process-job");

        System.out.println("Thread pool (reused workers):");
        AtomicInteger threadWorker = new AtomicInteger();
        ThreadFactory threadFactory = task -> {
            Thread thread = new Thread(task, "thread-worker-" + threadWorker.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
        runPool(new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(4), threadFactory), "thread-job");
    }

    private static void runPool(ThreadPoolExecutor pool, String jobPrefix) throws InterruptedException {
        try {
            for (int taskId = 1; taskId <= 5; taskId++) {
                int id = taskId;
                pool.submit(() -> System.out.printf("%s handled %s-%d%n",
                        Thread.currentThread().getName(), jobPrefix, id));
            }
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Worker pool did not finish in time");
            }
        } catch (InterruptedException exception) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
            throw exception;
        } catch (RuntimeException exception) {
            pool.shutdownNow();
            throw exception;
        }
    }
}
