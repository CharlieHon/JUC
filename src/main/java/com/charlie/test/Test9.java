package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

/**
 * interrupt()打断正常状态下的线程
 * 打断正常状态下的线程，不会清空打断状态
 */
@Slf4j(topic = "c.Test9")
public class Test9 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                // 拿到当前线程，即t1
                Thread currentThread = Thread.currentThread();
                // 获取打断标记
                boolean interrupted = currentThread.isInterrupted();
                if (interrupted) {
                    log.debug("被打断了，退出循环...");
                    break;
                }
            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
    }
}
