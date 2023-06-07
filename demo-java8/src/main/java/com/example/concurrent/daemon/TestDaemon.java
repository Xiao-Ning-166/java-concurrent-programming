package com.example.concurrent.daemon;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.daemon")
public class TestDaemon {

    public static void main(String[] args) throws InterruptedException {
        log.debug("开始运行。。。。。。");

        Thread thread = new Thread(() -> {
            log.debug("线程开始运行。。。。。。");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("线程运行结束。。。。。。");
        }, "daemon");

        // 设置线程为守护线程
        thread.setDaemon(true);
        thread.start();

        TimeUnit.SECONDS.sleep(1);

        log.debug("运行结束。。。。。。");
    }

}
