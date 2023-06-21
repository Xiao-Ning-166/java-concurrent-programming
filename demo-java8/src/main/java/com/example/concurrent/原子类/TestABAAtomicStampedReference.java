package com.example.concurrent.原子类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import static java.lang.Thread.sleep;

@Slf4j(topic = "log.aba-atomicStampedReference")
public class TestABAAtomicStampedReference {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 1);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        // 这个共享变量被它线程修改过？
        String prev = ref.getReference();
        // 获取版本号
        int oldStamp = ref.getStamp();
        log.debug("旧版本号：{}", oldStamp);
        other();
        sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", oldStamp, oldStamp + 1));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            String oldValue = ref.getReference();
            int oldStamp = ref.getStamp();
            log.debug("change A->B {}", ref.compareAndSet(oldValue, "B", oldStamp, oldStamp + 1));
            log.debug("更新版本号：{}", ref.getStamp());
        }, "t1").start();

        TimeUnit.MILLISECONDS.sleep(500);

        new Thread(() -> {
            String oldValue = ref.getReference();
            int oldStamp = ref.getStamp();
            log.debug("change B->A {}", ref.compareAndSet(oldValue, "A", oldStamp, oldStamp + 1));
            log.debug("更新版本号：{}", ref.getStamp());
        }, "t2").start();
    }

}
