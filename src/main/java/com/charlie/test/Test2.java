package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask配合Thread
 */
@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("running");
                Thread.sleep(1000);
                return 100;
            }
        });

        // 参数1是任务对象；参数2是线程名
        Thread t = new Thread(task, "t1");
        t.start();

        // 主线程阻塞，同步等待 task 执行完毕的结果
        log.debug("{}", task.get());    // 100
        //Integer result = task.get();
    }
}
