package com.lowleveldesign.subjects.concurrency_design.producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class BoundedBufferDemo {
    private BoundedBufferDemo() {
    }

    public static void run() throws InterruptedException {
        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(1);
        Thread producer = new Thread(() -> buffer.add("message"), "producer");
        producer.start();
        producer.join();
        System.out.println("Producer-consumer: received " + buffer.take());
    }
}
