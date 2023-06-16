package com.example.concurrent.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;


@Slf4j(topic = "log.reentrant-lock")
public class TestReentrantLock1 {

    static ReentrantLock lock = new ReentrantLock();
    
    public static void main(String[] args) {
        method1();
    }

    private static void method1() {
        lock.lock();
        try {
            log.debug("execute method1()...");
            method2();
        } finally {
            lock.unlock();
        }
    }

    private static void method2() {
        lock.lock();
        try {
            log.debug("execute method2()...");
            method3();
        } finally {
            lock.unlock();
        }
    }

    private static void method3() {
        lock.lock();
        try {
            log.debug("execute method3()...");
        } finally {
            lock.unlock();
        }
    }

}
