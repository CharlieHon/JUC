package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 方法一，直接使用 Thread
 * 把线程和任务合并在一起
 */
@Slf4j(topic = "c.Test1")
public class Test1 {

    public static void main(String[] args) {
        // 创建线程，构造方法的参数是给线程指定名字（推荐）
        Thread t = new Thread("t1") {
            @Override
            public void run() {
                // 要执行的任务
                log.debug("running");
            }
        };

        // 指定名称
        //t.setName("t1");
        // 启动线程
        t.start();

        log.debug("running");
    }

    /**
     * 方法二，使用 Runnable 配合 Thread
     * 把线程和任务分开了
     */
    public static void test() {
        // Runnable可运行的任务
        //Runnable r = new Runnable() {
        //    @Override
        //    public void run() {
        //        log.debug("running");
        //    }
        //};

        // 使用lambda表达式简化代码，因为Runnable是函数式接口，只有一个抽象方法
        Runnable r = () -> {
            log.debug("running");
        };

        // Thread代表线程
        Thread t = new Thread(r, "t2");
        t.start();  // 启动线程
    }

}
