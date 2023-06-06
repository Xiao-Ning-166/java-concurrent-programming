package com.example.concurrent.method;

import lombok.extern.slf4j.Slf4j;

import java.time.chrono.IsoChronology;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "log.interrupt")
public class TestInterrupt {

    public static void main(String[] args) throws InterruptedException {
        // test1();

        // test2();

        interruptParkThread();
    }

    /**
     * 打断park线程
     */
    private static void interruptParkThread() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park......");

            LockSupport.park();

            log.debug("unpark......");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
        }, "t1");

        t1.start();

        TimeUnit.SECONDS.sleep(1);

        t1.interrupt();
    }

    private static void test2() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                boolean interrupted = currentThread.isInterrupted();
                if (interrupted) {
                    log.debug("被打断了，打断状态：{}", interrupted);
                    break;
                }
            }
        }, "t2");

        t2.start();

        TimeUnit.MILLISECONDS.sleep(1000L);
        // 打断线程
        log.debug("interrupt......");
        t2.interrupt();

    }

    private static void test1() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                log.debug("sleep......");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");

        t1.start();

        TimeUnit.MILLISECONDS.sleep(500L);
        // 打断线程
        log.debug("interrupt......");
        t1.interrupt();
        // 获取打断状态
        log.debug("打断状态：{}", t1.isInterrupted());

    }

}
