package com.example.concurrent.原子类;

import jdk.internal.misc.Unsafe;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j(topic = "log.unsafe")
public class TestUnsafe {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        test1();
    }

    private static void test1() throws NoSuchFieldException, IllegalAccessException {

        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);

    }

}
