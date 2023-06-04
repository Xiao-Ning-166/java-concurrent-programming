package com.example.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 方法二：实现Runnable接口
 * 1、创建 Runnbale 实现类
 * 2、重写 run()
 * 3、创建实现类对象
 * 4、创建 Thread 对象，将实现类对象作为参数传入
 * 5、调用 Thread 对象的start()，启动线程
 *
 */
@Slf4j(topic = "log.create-thread2")
public class CreatThread2 {

    public static void main(String[] args) {
        // 3、创建Runnable实现类对象
        Runnable1 runnable1 = new Runnable1();

        // 4、创建 Thread 对象，将实现类对象作为参数传入
        Thread thread2 = new Thread(runnable1);
        thread2.setName("线程2");

        // 5、调用 start() 方法，启动线程
        thread2.start();

        log.debug("main thread is running");

    }

}

@Slf4j(topic = "log.runnable")
class Runnable1 implements Runnable {

    @Override
    public void run() {
        log.debug("Runnable is running");
    }
}
