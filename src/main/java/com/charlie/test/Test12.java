package com.charlie.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test12")
public class Test12 {

    public static void main(String[] args) throws InterruptedException {

        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");
        t2.start();

        t1.join();
        t2.join();
        log.debug("counter = {}", room.getCounter());
    }
}

// 面向对象改进——把需要保护的共享变量放入一个类
class Room {
    private int counter = 0;

    // 在成员方法上加锁，等价于下面加锁方式
    public synchronized void increment() {
            counter++;
    }

    // 获取值，也需要加锁，防止拿到中间状态的值
    public void decrement() {
        synchronized (this) {
            counter--;
        }
    }

    public int getCounter() {
        synchronized (this) {
            return counter;
        }
    }
}
