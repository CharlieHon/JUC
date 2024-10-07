package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static com.charlie.util.Sleeper.sleep;

/**
 * <h3>打断park线程</h3>
 * interrupt()打断park线程，不会清空打断标记
 */
@Slf4j(topic = "c.Test10")
public class Test10 {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpack...");
            log.debug("打断标记：{}", Thread.currentThread().isInterrupted());   // true
            //log.debug("打断标记：{}", Thread.interrupted()); // Thread.interrupted()会重置打断标记
            LockSupport.park(); // 当打断标记为true时，park会失效
            log.debug("unpack...");
        }, "t1");

        t1.start();

        sleep(1);
        t1.interrupt();
    }
}
