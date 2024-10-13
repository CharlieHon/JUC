package com.charlie.pattern;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static com.charlie.util.Sleeper.sleep;

/**
 * 同步模式-保护性暂停
 */
@Slf4j(topic = "c.Test2")
public class Test2 {
    // 线程1等待线程2的下载结果
    public static void main(String[] args) {

        GuardedObject guardedObject = new GuardedObject();

        // 普通等待
        //new Thread(() -> {
        //    // 等待结果
        //    log.debug("等待结果");
        //    //List<String> list = (List<String>) guardedObject.get();
        //    List<String> list = (List<String>) guardedObject.get(500);
        //    log.debug("结果的大小：{}", list.size());
        //}, "t1").start();
        //
        //new Thread(() -> {
        //    log.debug("执行下载");
        //    try {
        //        List<String> list = Downloader.download();
        //        guardedObject.complete(list);
        //    } catch (IOException e) {
        //        throw new RuntimeException(e);
        //    }
        //}, "t2").start();

        // 超时等待
        new Thread(() -> {
            log.debug("begin");
            Object obj = guardedObject.get(2000);
            log.debug("结果是:{}", obj);
        }, "t3").start();

        new Thread(() -> {
            log.debug("begin");
            sleep(1);
            guardedObject.complete(null);
            log.debug("end");
        }, "t4").start();
    }
}

class GuardedObject {
    // 结果
    private Object response;

    // 获取结果
    public Object get() {
        synchronized (this) {
            // 没有结果
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return response;
        }
    }

    // 获取结果-设置超时时间timeout ms
    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                // 经历的时间超过timeout时，退出循环
                if (waitTime <= 0) {
                    break;
                }
                try {
                    // timeout - passedTime 避免因为 虚假唤醒 导致重新等待timeout，使等待时间变长
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 求得经历时间
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
