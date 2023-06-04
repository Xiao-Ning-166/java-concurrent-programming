package com.example.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程方法一：继承Thread类
 *
 * 1、继承Thread类
 * 2、重写 run()
 * 3、创建实例
 * 4、调用 start()
 *
 */
@Slf4j(topic = "log.create-thread1")
public class CreateThread1 {

    public static void main(String[] args) {
        Thread1 thread1 = new Thread1();
        thread1.setName("线程1");
        thread1.start();

        log.debug("main thread is running");
    }

}

@Slf4j(topic = "log.thread1")
class Thread1 extends Thread {

    @Override
    public void run() {
        log.debug("thread1 is running");
    }
}
