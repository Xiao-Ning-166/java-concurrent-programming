package com.example.concurrent.synchroniz;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "log.Synchronized")
public class TestSynchronized2 {

    public static void main(String[] args) throws InterruptedException {

        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("结果：{}", room.getCount());
    }

}

class Room {

    private int counter = 0;

    public void increment() {
        synchronized (this) {
            this.counter++;
        }
    }

    public void decrement() {
        synchronized (this) {
            this.counter--;
        }
    }

    public int getCount() {
        // 加锁是为了防止访问中间值
        synchronized (this) {
            return this.counter;
        }
    }

}
