package com.example.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 方法3：实现Callable接口
 * 1、创建 Callable 接口实现类
 * 2、实现 call() 方法
 * 3、创建 FutureTask 对象，将 Callable 实现类对象作为参数传入
 * 4、创建 Thread 对象，将 FutureTask 对象作为参数传入
 * 5、调用 Thread 对象的 start()，启动线程
 * 6、调用 FutureTask 的 get() 得到线程返回值
 */
@Slf4j(topic = "log.create-thread3")
public class CreateThread3 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建 FutureTask对象，将Callable实现类作为参数传入
        FutureTask<Integer> integerFutureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("线程要做的事。。。。。。");
                return 1;
            }
        });

        // 创建 Thread 对象，并将 FutureTask 对象作为参数传入
        Thread thread3 = new Thread(integerFutureTask, "线程3");

        // 调用 start()，启动线程
        thread3.start();

        // 调用 get() 得到返回值
        Integer futureTaskResult = integerFutureTask.get();
        log.debug("线程返回值：{}", futureTaskResult);

        log.debug("main thread is running");
    }

}
