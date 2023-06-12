package com.example.concurrent.synchroniz;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "log.biased")
public class TestBiased {

    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        dog.setName("阿黄");
        dog.setAge(3);
        String printable = ClassLayout.parseInstance(dog).toPrintable();
        log.debug(printable);

        TimeUnit.SECONDS.sleep(5);

        Dog dog2 = new Dog();
        dog2.setName("小白");
        dog2.setAge(3);
        String printable2 = ClassLayout.parseInstance(dog2).toPrintable();
        log.debug(printable2);

    }

}

@Data
class Dog {
    private String name;

    private Integer age;
}
