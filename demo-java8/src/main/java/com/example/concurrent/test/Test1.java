package com.example.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 烧水泡茶
 */
@Slf4j(topic = "log.test1")
public class Test1 {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            log.debug("洗水壶。。。。。。");
            try {
                TimeUnit.SECONDS.sleep(1);
                log.debug("烧开水。。。。。。");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "老王");


        Thread t2 = new Thread(() -> {
            try {
                log.debug("洗茶壶。。。。。。");
                log.debug("洗茶杯。。。。。。");
                log.debug("拿茶叶。。。。。。");
                TimeUnit.SECONDS.sleep(4);

                t1.join();
                log.debug("开始泡茶。。。。。。。。");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "老李");

        t1.start();
        t2.start();

    }

}
