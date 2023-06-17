package com.example.concurrent.可见性;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.TestVolatile1")
public class TestVolatile1 {

    volatile static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        // test1();
        test2();
    }

    static boolean run2 = true;

    static final Object lock = new Object();

    private static void test2() throws InterruptedException {
        Thread t = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (!run2) {
                        break;
                    }
                }
            }
        });

        t.start();
        TimeUnit.SECONDS.sleep(1);

        log.debug("停止 t");
        synchronized (lock) {
            run2 = false;
        }

    }

    private static void test1() throws InterruptedException {
        new Thread(() -> {
            while (run) {

            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("停止 t");
        run = false;
    }

}
