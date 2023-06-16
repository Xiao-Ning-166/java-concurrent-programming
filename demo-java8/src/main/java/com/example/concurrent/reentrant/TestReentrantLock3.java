package com.example.concurrent.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁超时
 */
@Slf4j(topic = "log.reentrant-lock")
public class TestReentrantLock3 {

    static ReentrantLock lock = new ReentrantLock();
    
    public static void main(String[] args) throws InterruptedException {
        test1();

        // test2();
    }

    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取等待 1s 后失败，返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } finally {
            lock.unlock();
        }

    }

    private static void test1() {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获取锁");
            if (!lock.tryLock()) {
                log.debug("获取不到锁，立刻失败");
                return;
            }
            try {
                log.debug("获得到了锁");
            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        t1.start();
    }
}
