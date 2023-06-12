package com.example.concurrent.safe;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;


@Slf4j(topic = "log.safe")
public class TestThreadSafe {

    static final int THREAD_NUMBER = 2;

    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        // testMemberVariable();

        testLocalVariable();
    }

    private static void testLocalVariable() {
        ThreadSafe threadSafe = new ThreadSafe();

        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadSafe.method1(LOOP_NUMBER);
            }, "Thread-" + (i + 1)).start();
        }
    }

    private static void testMemberVariable() {
        ThreadUnsafe threadUnsafe = new ThreadUnsafe();

        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadUnsafe.method1(LOOP_NUMBER);
            }, "Thread-" + (i + 1)).start();
        }
    }
}

class ThreadSafe {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }

}

class ThreadUnsafe {

    ArrayList<String> list = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }

}

