package com.example.concurrent.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Slf4j(topic = "log.join")
public class TestJoin {

    static int r = 0;
    static int r1 = 0;
    static int r2 = 0;
    static int r3 = 0;

    public static void main(String[] args) throws InterruptedException {
        // test1();

        /**
         * 使用 join 等待多个
         */
        // test2();

        /**
         * 限时等待
         */
        test3();
    }

    /**
     * 限时等待
     */
    private static void test3() throws InterruptedException {
        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                r3 = 20;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t3");

        t3.start();
        log.debug("join begin");
        t3.join(1500);
        log.debug("r3 = {}", r3);
    }

    /**
     * 使用 join 等待多个
     *
     * @throws InterruptedException
     */
    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            r1 = 10;
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2");

        t1.start();
        t2.start();

        long start = System.currentTimeMillis();
        log.debug("join begin");
        t1.join();
        log.debug("t1 join end");
        t2.join();
        log.debug("t2 join end");
        long end = System.currentTimeMillis();

        log.debug("r1 = {}；r2 = {}； cost = {}ms", r1, r2, (end - start));
    }

    private static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                sleep(1000);
                log.debug("结束");
                r = 10;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");

        t1.start();
        // t1.join();
        log.debug("结果 r = {}", r);
        log.debug("结束");

        /**
         * 分析
         *
         * 因为主线程和线程t1是并行执行的，t1线程需要1秒之后才能算出 r = 10
         * 而主线程一开始就要打印 r 的结果，所以只能打印出 r = 0
         */
    }

}
