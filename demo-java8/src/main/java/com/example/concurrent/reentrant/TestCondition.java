package com.example.concurrent.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.VarHandle;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试 ReentrantLock 条件变量
 */
@Slf4j(topic = "log.condition")
public class TestCondition {

    static ReentrantLock lock = new ReentrantLock();

    // 等烟的休息室
    static Condition waitCigaretteQueue = lock.newCondition();

    // 等外卖的休息室
    static Condition waitTakeQueue = lock.newCondition();

    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        // 进入等烟的休息室
                        waitCigaretteQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟了，可以开始干活了");
            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        // 进入等外卖的休息室
                        waitTakeQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖到了，可以开始干活了");

            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "小女").start();

        Thread.sleep(1);
        new Thread(() -> {
            lock.lock();
            try {
                hasTakeout = true;
                log.debug("外卖到了噢！");
                // 通知等外卖休息室的
                waitTakeQueue.signalAll();
            } finally {
                lock.unlock();
            }
        }, "送外卖的").start();

        new Thread(() -> {
            lock.lock();
            try {
                hasCigarette = true;
                log.debug("烟到了噢！");
                // 通知等烟休息室的
                waitCigaretteQueue.signalAll();
            } finally {
                lock.unlock();
            }
        }, "送烟的").start();
    }

}
