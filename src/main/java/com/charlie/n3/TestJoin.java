package com.charlie.n3;

import lombok.extern.slf4j.Slf4j;
import static com.charlie.util.Sleeper.sleep;


/**
 * join()方法
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {

    static int r = 0;
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        //test1();
        //test2();
        test3();
    }

    // t1.join(3000)最多等待3000ms，当t1线程提前结束时，join也结束
    public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(2);
            r1 = 10;
        });

        long start = System.currentTimeMillis();
        t1.start();

        log.debug("join begin");
        // t1线程执行结束会导致 join 结束
        t1.join(3000);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

    // 执行t1.join()会让当前线程停下等待t1执行完，但是并不影响t2线程执行
    public static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(1);
            r1 = 10;
        });
        Thread t2 = new Thread(() -> {
            sleep(2);
            r2 = 20;
        });
        t1.start();
        t2.start();
        long start = System.currentTimeMillis();
        log.debug("join begin");
        t1.join();
        log.debug("t1 join end");
        t2.join();
        log.debug("t2 join end");
        long end = System.currentTimeMillis();
        // r1: 10 r2: 20 cost: 2009 耗时2s
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

    public static void test1() throws InterruptedException {
        log.debug("begin...");
        Thread t1 = new Thread(() -> {
            log.debug("begin...");
            sleep(1);
            log.debug("ending...");
            r = 10;
        }, "t1");
        t1.start();
        // t1.join()，主线程会等待t1线程结束后，再执行
        t1.join();
        log.debug("r = {}", r);
        log.debug("ending...");
    }
}
