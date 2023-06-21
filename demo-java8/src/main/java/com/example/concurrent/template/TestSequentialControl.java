package com.example.concurrent.template;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 顺序控制
 */
@Slf4j(topic = "log.sequential-control")
public class TestSequentialControl {

    static final Object lock = new Object();

    /**
     * 2是否打印
     */
    static boolean t2Runed = false;

    public static void main(String[] args) throws InterruptedException {
        // method1();

        // method2();

        // method3();

        // method4();

        method5();
    }

    static Thread t1;
    static Thread t2;
    static Thread t3;

    private static void method5() {
        ParkUnparkMethod parkUnparkMethod = new ParkUnparkMethod(1000);

        t1 = new Thread(() -> {
            parkUnparkMethod.print("a", t2);
        });
        t2 = new Thread(() -> {
            parkUnparkMethod.print("b", t3);
        });
        t3 = new Thread(() -> {
            parkUnparkMethod.print("c\n", t1);
        });

        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }

    private static void method4() throws InterruptedException {

        ReentrantLockMethod reentrantLockMethod = new ReentrantLockMethod(1000);
        // 创建三个条件变量
        Condition a = reentrantLockMethod.newCondition();
        Condition b = reentrantLockMethod.newCondition();
        Condition c = reentrantLockMethod.newCondition();

        new Thread(() -> {
            reentrantLockMethod.print("a", a, b);
        }).start();

        new Thread(() -> {
            reentrantLockMethod.print("b", b, c);
        }).start();

        new Thread(() -> {
            reentrantLockMethod.print("c\n", c, a);
        }).start();

        TimeUnit.SECONDS.sleep(1);

        reentrantLockMethod.lock();
        log.debug("开始");
        try {
            a.signal();
        } finally {
            reentrantLockMethod.unlock();
        }
    }

    private static void method3() {
        WaitNotifyMehtod waitNotifyMehtod = new WaitNotifyMehtod(1, 1000);

        new Thread(() -> {
            waitNotifyMehtod.print("a", 1, 2);
        }).start();
        new Thread(() -> {
            waitNotifyMehtod.print("b", 2, 3);
        }).start();
        new Thread(() -> {
            waitNotifyMehtod.print("c\n", 3, 1);
        }).start();
    }

    /**
     * 使用 park/unpark实现先打印2后打印1
     */
    private static void method2() {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");

        t1.start();

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        }, "t2");

        t2.start();
    }

    /**
     * 使用 wait/notify实现先打印2后打印1
     */
    private static void method1() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!t2Runed) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 2已经打印，可以打印1
                log.debug("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                t2Runed = true;
                lock.notifyAll();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}

/**
 * park/unpak实现顺序打印abcabcabcabcabc
 */
class ParkUnparkMethod {

    private int loopNumber;

    public ParkUnparkMethod(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     *
     * @param content 打印内容
     * @param next 下一个线程
     */
    public void print(String content, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            // 等待
            LockSupport.park();
            System.out.print(content);
            // 唤醒下一个线程
            LockSupport.unpark(next);
        }
    }
}

/**
 * ReentrantLock实现顺序打印abcabcabcabcabc
 */
class ReentrantLockMethod extends ReentrantLock {

    private int loopNumber;

    public ReentrantLockMethod(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * 打印
     *
     * @param content 打印内容
     * @param current 当前线程所在休息室
     * @param next 下一个线程所在休息是
     */
    public void print(String content, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                // 进入等待
                current.await();
                // 打印
                System.out.print(content);
                // 唤醒下一个休息室的线程
                next.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.unlock();
            }
        }
    }

}

/**
 * wait/motify实现顺序abcabcabcabcabc
 */
class WaitNotifyMehtod {

    /**
     * 打印标记。1打印a，2打印b，3打印c
     */
    private int flag;

    /**
     * 循环次数
     */
    private int loopNumber;

    public WaitNotifyMehtod(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    /**
     * 打印
     *
     * @param content  打印内容
     * @param waitFlag 等待标记。与打印标记对比，一致则打印出打印内容
     * @param nextFlag 下一个标记
     */
    public void print(String content, int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (waitFlag != flag) {
                    try {
                        // 等待标记与打印标记不一致，等待
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 一致，打印
                System.out.print(content);
                // 设置为下一个标记
                flag = nextFlag;
                // 唤醒其他线程
                this.notifyAll();
            }
        }
    }

}
