package com.example.concurrent.template;


public class TestSingletion {
}

class Singleton {

    private static Singleton instance = null;

    public Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Singleton();
        return instance;
    }

}
