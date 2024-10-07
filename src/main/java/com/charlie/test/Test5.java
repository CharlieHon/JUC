package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

// Thread.sleep
@Slf4j(topic = "c.Test5")
public class Test5 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state: {}", t1.getState());   // RUNNABLE，因为main线程优先级高，此时t1还没执行到sleep方法

        // 主线程休眠500ms，Thread.sleep在哪个线程中调用，就是哪个线程休眠
        Thread.sleep(500);
        log.debug("t1 state: {}", t1.getState());   // TIMED_WAITING
    }
}
