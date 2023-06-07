package com.example.concurrent.state;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "log.thread-state")
public class TestThreadState {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("t1 is running");
            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {

                }
            }
        };
        t2.start();

        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("t3 is runing");
            }
        };
        t3.start();

        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (TestThreadState.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        t5.start();

        Thread t6 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (TestThreadState.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t6.start();

        // 打印线程状态
        log.debug("t1 state：{}", t1.getState());
        log.debug("t2 state：{}", t2.getState());
        log.debug("t3 state：{}", t3.getState());
        log.debug("t4 state：{}", t4.getState());
        log.debug("t5 state：{}", t5.getState());
        log.debug("t6 state：{}", t6.getState());
    }

}
