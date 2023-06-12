package com.example.concurrent.safe;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;


@Slf4j(topic = "log.safe")
public class TestThreadSafe2 {

    static final int THREAD_NUMBER = 2;

    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadSafeSubClass threadSafe2 = new ThreadSafeSubClass();

        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadSafe2.method1(LOOP_NUMBER);
            }, "Thread-" + (i + 1)).start();
        }
    }
}

class ThreadSafe2 {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list) {
        list.add("1");
    }

    public void method3(ArrayList<String> list) {
        list.remove(0);
    }

}

class ThreadSafeSubClass extends ThreadSafe2 {

    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }, "sub-thread").start();
    }
}

