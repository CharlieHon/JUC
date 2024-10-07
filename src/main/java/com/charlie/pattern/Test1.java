package com.charlie.pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * 两阶段终止模式1：使用interrupt打断线程，在捕获异常中重新设置interrupt状态
 */
@Slf4j(topic = "c.Test1")
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("料理后事...");
                    break;
                }
                // 未被打断，每隔1s执行监控任务
                try {
                    Thread.sleep(1000);     // 情况1：阻塞状态，抛出异常并清空打断标记
                    log.debug("执行监控记录...");   // 情况2：正常状态，打断标记不会被清空
                } catch (InterruptedException e) {  // 捕获异常后，会将打断标记清空
                    e.printStackTrace();
                    // 重新设置打断标记
                    current.interrupt();
                }
            }
        });

        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}
