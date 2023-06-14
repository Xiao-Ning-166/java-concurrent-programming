package com.example.concurrent.template;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 保护性暂停
 */
@Slf4j(topic = "log.GuardedSuspenson")
public class TestGuardedSuspenson {

    public static void main(String[] args) throws InterruptedException {
        // 普通版
        // test1();

        // 带超时版
        // test2();

        test3();

    }

    private static void test3() throws InterruptedException {
        // 居民等待收信
        for (int i = 0; i < 3; i++) {
            new Thread(new People()).start();
        }

        TimeUnit.SECONDS.sleep(3);
        // 邮差送信
        for (Integer id : MailBox.getIds()) {
            Postman postman = new Postman(id, "居民-" + id + " 的信");
            new Thread(postman).start();
        }
    }

    private static void test2() {
        TimedGuardedObject<Boolean> timedGuardedObject = new TimedGuardedObject<>();
        new Thread(() -> {
            log.debug("等待结果");
            // 等待结果
            Boolean response = timedGuardedObject.getResponse(2000);
            log.debug("等到结果，结果是：{}", response);
        }, "t1").start();

        new Thread(() -> {
            try {
                log.debug("产生结果");
                TimeUnit.SECONDS.sleep(5);
                // 产生结果
                // timedGuardedObject.setResponse(true);
                timedGuardedObject.setResponse(null);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();
    }

    private static void test1() {
        GuardedObject<Boolean> guardedObject = new GuardedObject<>();
        new Thread(() -> {
            log.debug("等待结果");
            // 等待结果
            guardedObject.getResponse();
            log.debug("等到结果");
        }, "t1").start();

        new Thread(() -> {
            try {
                log.debug("产生结果");
                TimeUnit.SECONDS.sleep(5);
                // 产生结果
                guardedObject.setResponse(true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();
    }

}

@Slf4j(topic = "log.people")
class People implements Runnable {

    @Override
    public void run() {
        // 生成一个Guarded Object
        GuardedObjectV3 guardedObject = MailBox.createGuardedObject();
        // 等待收信
        log.debug("居民-{}，等待收信", guardedObject.getId());
        Object response = guardedObject.getResponse(5000);
        log.debug("居民-{}，收到信，内容：{}", guardedObject.getId(), response);
    }
}

@Slf4j(topic = "log.Postman")
class Postman implements Runnable {

    private int id;

    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        // 开始送信
        GuardedObjectV3 guardedObject = MailBox.getGuardedObject(id);
        log.debug("开始送信，居民-{}", guardedObject.getId());
        guardedObject.setResponse(mail);
    }
}

class MailBox {
    private static Map<Integer, GuardedObjectV3> box = new Hashtable<>();

    private static int id;

    /**
     * 生成唯一id
     *
     * @return
     */
    public static synchronized int generatedId() {
        return id++;
    }

    public static GuardedObjectV3 getGuardedObject(int id) {
        return box.remove(id);
    }

    public static GuardedObjectV3 createGuardedObject() {
        GuardedObjectV3<Object> objectGuardedObject = new GuardedObjectV3<>(generatedId());
        box.put(objectGuardedObject.getId(), objectGuardedObject);
        return objectGuardedObject;
    }

    public static Set<Integer> getIds() {
        return box.keySet();
    }
}

/**
 * 解耦等待和生产
 */
class GuardedObjectV3<T> {

    /**
     * 唯一标识 Guarded Object
     */
    private int id;

    /**
     * 结果
     */
    private T response;

    public GuardedObjectV3() {
    }

    public GuardedObjectV3(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取结果
     *
     * @param timeout 超时时间
     * @return
     */
    public T getResponse(long timeout) {
        synchronized (this) {
            // 开始时间
            long start = System.currentTimeMillis();
            // 经历时间
            long passedTime = 0;
            while (this.response == null) {
                try {
                    // 本轮循环应该等待的时间
                    long waitTime = timeout - passedTime;
                    // 判断是否超时
                    if (waitTime <= 0) {
                        // 超时
                        break;
                    }
                    // 等待 timeout - passedTime，避免虚假唤醒时等待时间变长
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 计算经历时间
                passedTime = System.currentTimeMillis() - start;
            }
            return this.response;
        }
    }

    /**
     * 产生结果
     *
     * @param response
     */
    public void setResponse(T response) {
        synchronized (this) {
            this.response = response;
            // 唤醒等待的线程
            this.notifyAll();
        }
    }
}

/**
 * 带超时版
 *
 * @param <T>
 */
class TimedGuardedObject<T> {

    /**
     * 结果
     */
    private T response;

    /**
     * 获取结果
     *
     * @param timeout 超时时间
     * @return
     */
    public T getResponse(long timeout) {
        synchronized (this) {
            // 开始时间
            long start = System.currentTimeMillis();
            // 经历时间
            long passedTime = 0;
            while (this.response == null) {
                try {
                    // 本轮循环应该等待的时间
                    long waitTime = timeout - passedTime;
                    // 判断是否超时
                    if (waitTime <= 0) {
                        // 超时
                        break;
                    }
                    // 等待 timeout - passedTime，避免虚假唤醒时等待时间变长
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 计算经历时间
                passedTime = System.currentTimeMillis() - start;
            }
            return this.response;
        }
    }

    /**
     * 产生结果
     *
     * @param response
     */
    public void setResponse(T response) {
        synchronized (this) {
            this.response = response;
            // 唤醒等待的线程
            this.notifyAll();
        }
    }
}

/**
 * 普通版
 *
 * @param <T>
 */
class GuardedObject<T> {

    private T response;

    /**
     * 获取结果
     *
     * @return
     */
    public T getResponse() {
        synchronized (this) {
            // 条件不满足则等待
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return this.response;
        }
    }

    /**
     * 产生结果
     *
     * @param response
     */
    public void setResponse(T response) {
        synchronized (this) {
            // 条件满足，通知线程
            this.response = response;
            this.notifyAll();
        }
    }

}
