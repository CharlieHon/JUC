package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

// interrupt()
/*
14:26:45.459 c.Test6 [t1] - enter sleep...
14:26:46.466 c.Test6 [main] - interrupt...
14:26:46.466 c.Test6 [t1] - wake up...
Exception in thread "t1" java.lang.RuntimeException:
    java.lang.InterruptedException: sleep interrupted
 */
@Slf4j(topic = "c.Test6")
public class Test6 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep...");
                try {
                    //Thread.sleep(2000);
                    TimeUnit.SECONDS.sleep(2000);   // 效果同Thread.sleep()，可读性更强
                } catch (InterruptedException e) {
                    log.debug("wake up...");
                    throw new RuntimeException(e);
                }
            }
        };
        t1.start();

        Thread.sleep(1000);
        log.debug("interrupt...");
        // 调用线程t1的interrupt()方法，打断t1的sleep
        t1.interrupt();
        log.debug("t1 interrupt...");
    }
}
