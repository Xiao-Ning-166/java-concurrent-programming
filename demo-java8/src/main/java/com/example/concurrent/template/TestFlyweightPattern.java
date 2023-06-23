package com.example.concurrent.template;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 享元模式
 */
@Slf4j(topic = "log.flyweiht-pattern")
public class TestFlyweightPattern {

    public static void main(String[] args) {
        MyDBPool myDBPool = new MyDBPool(2);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Connection coon = null;
                try {
                    coon = myDBPool.getConnection();
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    myDBPool.free(coon);
                }
            }).start();
        }
    }

}

@Slf4j(topic = "log.db-pool")
class MyDBPool {

    /**
     * 连接池的大小
     */
    private final int poolSize;

    /**
     * 连接池数组
     */
    private Connection[] connections;

    /**
     * 连接状态数组
     */
    private AtomicIntegerArray status;

    private final int AVAILABLE = 0;

    private final int UNAVAILABLE = 1;

    public MyDBPool(int poolSize) {
        this.poolSize = poolSize;
        this.connections = new Connection[poolSize];
        this.status = new AtomicIntegerArray(new int[poolSize]);
    }

    /**
     * 获取连接
     *
     * @return
     */
    public Connection getConnection() {
       while (true) {
           for (int i = 0; i < poolSize; i++) {
               if (this.status.compareAndSet(i, AVAILABLE, UNAVAILABLE)) {
                   log.debug("获取连接");
                   // 连接可用
                   return this.connections[i];
               }
           }
           // 无连接可用，进入等待
            synchronized (this) {
                try {
                    log.debug("进入等待");
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
       }
    }


    /**
     * 归还连接
     *
     * @param conn
     */
    public void free(Connection conn) {
        for (int i = 0; i < connections.length; i++) {
            if (conn == connections[i]) {
                log.debug("归还连接");
                this.status.set(i, AVAILABLE);
                // 通知等待线程
                synchronized (this) {
                    this.notifyAll();
                }
                break;
            }
        }
    }
}
