package com.example.concurrent.cpu;

import java.util.concurrent.TimeUnit;

/**
 * 防止CPU占用100%
 */
public class TestSleep {

    public static void main(String[] args) {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
