package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

/**
 * <h3> start与run </h3>
 * <lo>
 *     <li>
 *         直接调用run是在主线程中执行了run，没有启动新的线程
 *     </li>
 *     <li>
 *         使用start是启动新的线程，通过新的线程间接执行run中的代码
 *     </li>
 * </lo>
 */
@Slf4j(topic = "c.Test4")
public class Test4 {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
            }
        };

        // 是由主线程调用的
        //t1.run();   // c.Test4 [main] - running

        System.out.println(t1.getState());  // NEW
        t1.start();
        //t1.start(); // start方法不能多次调用 java.lang.IllegalThreadStateException
        System.out.println(t1.getState());  // RUNNABLE
    }
}
