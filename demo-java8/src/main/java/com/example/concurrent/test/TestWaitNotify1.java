package com.example.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.wait-notiy")
public class TestWaitNotify1 {

    final static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        // testWaitNotfiy();

        testWaitNotfiyAll();
    }

    private static void testWaitNotfiyAll() throws InterruptedException {
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    // 让线程在obj上一直等待下去
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        }).start();

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    // 让线程在obj上一直等待下去
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        }).start();

        // 主线程两秒后执行
        TimeUnit.SECONDS.sleep(2);
        log.debug("唤醒 obj 上其它线程");
        synchronized (obj) {
            // 唤醒obj上所有等待线程
            obj.notifyAll();
        }
    }


    private static void testWaitNotfiy() throws InterruptedException {
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    // 让线程在obj上一直等待下去
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        }).start();

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    // 让线程在obj上一直等待下去
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        }).start();

        // 主线程两秒后执行
        TimeUnit.SECONDS.sleep(2);
        log.debug("唤醒 obj 上其它线程");
        synchronized (obj) {
            obj.notify(); // 唤醒obj上一个线程
            // obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }

}
