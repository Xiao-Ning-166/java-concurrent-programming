package com.example.concurrent.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.SleepYieldDemo")
public class SleepYieldDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread sleepThread = new Thread("sleep-thread") {
            @Override
            public void run() {
                try {
                    log.debug("线程睡眠");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        sleepThread.start();
        log.debug("sleep-thread线程状态：{}", sleepThread.getState());
        Thread.sleep(500);

        log.debug("打断sleep-thread线程");
        sleepThread.interrupt();

        log.debug("sleep-thread线程状态：{}", sleepThread.getState());



        log.debug("睡眠前。。。。。。");
        TimeUnit.SECONDS.sleep(1);
        log.debug("睡眠后。。。。。。");
    }

}
