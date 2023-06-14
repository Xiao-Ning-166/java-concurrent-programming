package com.example.concurrent.template;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 生产者、消费者
 */
@Slf4j(topic = "log.ProducerConsumer")
public class TestProducerConsumer {
    public static void main(String[] args) {
        MessageQueue<String> messageQueue = new MessageQueue<>(5);

        for (int i = 0; i < 6; i++) {
            int id = i;
            new Thread(() -> {
                Message<String> message = new Message<>(id, "值" + id);
                messageQueue.put(message);
                log.debug("已生产消息");
            }, "生产者-" + id).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    Message<String> message = messageQueue.take();
                    log.debug("消费了消息：{}", message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "消费者").start();
    }
}

/**
 * 消息队列类，java线程间通信的实现
 */
@Slf4j(topic = "log.messageQueue")
class MessageQueue<T> {
    /**
     * 存放消息的容器
     */
    private LinkedList<Message<T>> container = new LinkedList<>();

    private int capacity;

    public MessageQueue() {
        this(10);
    }

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 消费消息
     *
     * @return
     */
    public Message<T> take() {
        synchronized (container) {
            while (container.isEmpty()) {
                try {
                    log.debug("队列空了，消费者等待");
                    container.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 返回队列头的消息
            Message<T> message = container.removeFirst();
            // 唤醒其他线程
            container.notifyAll();
            return message;
        }
    }

    /**
     * 生产消息
     *
     * @param message
     */
    public void put(Message<T> message) {
        synchronized (container) {
            while (container.size() >= this.capacity) {
                try {
                    log.debug("队列满了，生产者等待");
                    container.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 将消息存放到容器尾部
            container.addLast(message);
            // 唤醒其他线程
            container.notifyAll();
        }
    }
}

/**
 * 消息类
 */
final class Message<T> {
    private int id = 0;

    private T message;

    public Message(int id, T message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message=" + message +
                '}';
    }
}