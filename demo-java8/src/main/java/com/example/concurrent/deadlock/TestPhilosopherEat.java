package com.example.concurrent.deadlock;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * 哲学家就餐
 */
public class TestPhilosopherEat {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");

        new Thread(new Philosopher("苏格拉底", c1, c2), "苏格拉底").start();
        new Thread(new Philosopher("阿基米德", c2, c3), "阿基米德").start();
        new Thread(new Philosopher("亚历山大", c3, c4), "亚历山大").start();
        new Thread(new Philosopher("老子", c4, c5), "老子").start();
        new Thread(new Philosopher("庄子", c5, c1), "庄子").start();

    }

}

@Slf4j(topic = "log.philosopher")
class Philosopher implements Runnable {
    
    private String name;
    
    private Chopstick left;
    
    private Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            // 拿左手筷子
            synchronized (left) {
                // 拿右手筷子
                synchronized (right) {
                    // 吃饭
                    eat();
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }

    private void eat() {
        log.debug("eat......");
    }
}

/**
 * 筷子类
 */
class Chopstick {
    private String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" +
                "name='" + name + '\'' +
                '}';
    }
}

