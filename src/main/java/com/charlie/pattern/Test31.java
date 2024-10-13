package com.charlie.pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

import static com.charlie.util.Sleeper.sleep;

/**
 * <h3>异步模式-生产者/消费者</h3>
 * 21:19:56.484 c.MessageQueue [生产者0] - 已生产消息0
 * 21:19:56.486 c.MessageQueue [生产者2] - 已生产消息2
 * 21:19:56.486 c.MessageQueue [生产者1] - 队列已满，生产者线程等待
 * 21:19:57.487 c.MessageQueue [消费者] - 已消费消息0
 * 21:19:57.487 c.MessageQueue [生产者1] - 已生产消息1
 * 21:19:58.488 c.MessageQueue [消费者] - 已消费消息2
 * 21:19:59.501 c.MessageQueue [消费者] - 已消费消息1
 * 21:20:00.515 c.MessageQueue [消费者] - 队列为空，消费者线程等待
 */
@Slf4j(topic = "c.Test31")
public class Test31 {
    public static void main(String[] args) {

        MessageQueue queue = new MessageQueue(2);

        for (int i = 0; i < 3; i++) {
            int id = i; // lambda表达式中需要保证局部变量不被修改，因此不能设置为i
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + i).start();
        }

        new Thread(()-> {
            while (true) {
                sleep(1);
                Message message = queue.take();
            }
        }, "消费者").start();
    }
}

// 消息队列类，java线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {

    // 消息的队列集合
    private final LinkedList<Message> list = new LinkedList<>();
    // 队列容量
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 获取消息
    public Message take() {
        // 检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 从队列的头部获取消息并返回
            Message message = list.removeFirst();
            // 唤醒因为队列满，而进入waitSet的线程
            log.debug("已消费消息{}", message.getId());
            list.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        synchronized (list) {
            // 需要检查队列是否满了
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 将新的消息加入队列的尾部
            list.addLast(message);
            // wait-notifyAll 唤醒因为队列空，而进入 waitSet 的线程
            log.debug("已生产消息{}", message.getId());
            list.notifyAll();
        }
    }
}

// 线程之间通信，id很重要
@Slf4j(topic = "c.Message")
final class Message {
    private int id;
    private Object value;

    // 内部状态不可改变，只可在初始化时设置
    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}