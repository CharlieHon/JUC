package com.charlie.pattern;


import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import static com.charlie.util.Sleeper.sleep;

/**
 * <h3>多任务版GuardedObject</h3>
 * 使用 Mailboxes 解耦 结果产生的线程 和 结果接受的线程
 */
public class Test22 {
    public static void main(String[] args) {
        for (int i = 0; i <3; i++) {
            new People().start();
        }

        sleep(1);

        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "hello" + id).start();
        }
    }
}

@Slf4j(topic = "c.People")
class People extends Thread {
    @Override
    public void run() {
        // 收信
        GuardedObject1 guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}，内容:{}", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "c.Postman")
class Postman extends Thread {

    private int id;
    private String mail;

    @Override
    public void run() {
        GuardedObject1 guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("开始送信 id:{}，mail:{}", id, mail);
        guardedObject.complete(mail);
    }

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }
}

class Mailboxes {
    // 线程安全 Hashtable
    private static Map<Integer, GuardedObject1> boxes = new Hashtable<>();

    private static int id = 1;

    // 产生唯一id
    private static synchronized int generateId() {
        return id++;
    }

    // 根据id获取GuardedObject并删除
    public static GuardedObject1 getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObject1 createGuardedObject() {
        GuardedObject1 go = new GuardedObject1(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

class GuardedObject1 {
    // 表示 GuardedObject 对象
    private int id;
    // 结果
    private Object response;

    public GuardedObject1(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 等待结果-设置超时时间
    public Object get(int timeout) {
        synchronized (this) {
            long begin = System.currentTimeMillis();
            long passedTime = 0;
            while (response == null) {
                long waitTime = timeout - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
