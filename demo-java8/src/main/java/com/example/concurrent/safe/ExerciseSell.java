package com.example.concurrent.safe;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 卖票练习
 */
@Slf4j(topic = "log.sell")
public class ExerciseSell {
    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow ticketWindow = new TicketWindow(200);

        List<Integer> sellList = new Vector<>();
        List<Thread> threads = new ArrayList<>();
        // 创建两个线程买票
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int sell = ticketWindow.sell(randomAmount());
                // 统计卖票数
                sellList.add(sell);
            });
            threads.add(thread);
            thread.start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 统计卖出的票数和剩余的票数
        log.debug("余票数：{}", ticketWindow.getCount());
        // int amount = 0;
        // for (Integer count : sellList) {
        //     amount += count;
        // }
        log.debug("卖出票数：{}", sellList.stream().mapToInt(item -> item).sum());
    }
    // Random 为线程安全
    static Random random = new Random();
    // 随机 1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }
}
/**
 * 售票窗口类
 */
class TicketWindow {
    /**
     * 余票数量
     */
    private int count;
    public TicketWindow(int count) {
        this.count = count;
    }

    /**
     * 获取余票数
     */
    public int getCount() {
        return count;
    }

    /**
     * 售票
     */
    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
