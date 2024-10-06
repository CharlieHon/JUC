package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

/**
 * tasklist | findstr java          windows查询进程
 * taskkill /F /PID java进程编号     windows杀死java进程
 */
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        new Thread("t1") {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                }
            }
        }.start();

        new Thread("t2") {
            @Override
            public void run() {
                while (true) {
                    log.debug("running");
                }
            }
        }.start();
    }
}
