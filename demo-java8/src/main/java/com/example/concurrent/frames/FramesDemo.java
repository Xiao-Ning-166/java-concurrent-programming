package com.example.concurrent.frames;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.logging.XMLFormatter;

@Slf4j(topic = "log.frames")
public class FramesDemo {

    public static void main(String[] args) {
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }

}
