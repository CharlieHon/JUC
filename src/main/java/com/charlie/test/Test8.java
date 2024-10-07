package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

/**
 * interrupt()打断阻塞状态下的线程(sleep, wait, join)
 * 打断阻塞状态下的线程，会抛出异常，并将打断标记清空
 */
@Slf4j(topic = "c.Test8")
public class Test8 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("sleep...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");

        t1.start();
        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
        log.debug("打断标记:{}", t1.isInterrupted());   // false
    }
}
