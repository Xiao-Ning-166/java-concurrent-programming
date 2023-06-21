package com.example.concurrent.原子类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Slf4j(topic = "log.atom-integer")
public class TestAtomicInteger {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();

        // 获取并自增(i = 0，结果 i = 1，返回0) 类似 i++
        System.out.println(atomicInteger.getAndIncrement());

        // 自增并获取（i = 1， 结果 i = 2，返回 2）类似 ++i
        System.out.println(atomicInteger.incrementAndGet());

        // 自减并获取(i = 2，结果 i = 1，返回1) 类似 --i
        System.out.println(atomicInteger.decrementAndGet());

        // 获取并自减（i = 1， 结果 i = 0，返回 1）类似 i--
        System.out.println(atomicInteger.decrementAndGet());

        // 获取并加值（i = 0，结果 i = 5，返回 0）
        System.out.println(atomicInteger.getAndAdd(5));

        // 加值并获取（i = 5, 结果 i = 0, 返回 0）
        System.out.println(atomicInteger.addAndGet(-5));

        // 获取并更新（i = 0, p 为 i 的当前值, 结果 i = -2, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(atomicInteger.getAndUpdate(p -> p - 2));

        // 更新并获取（i = -2, p 为 i 的当前值, 结果 i = 0, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(atomicInteger.updateAndGet(p -> p + 2));

        // 获取并计算（i = 0, p 为 i 的当前值, x 为参数1, 结果 i = 10, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        // getAndUpdate 如果在 lambda 中引用了外部的局部变量，要保证该局部变量是 final 的
        // getAndAccumulate 可以通过 参数1 来引用外部的局部变量，但因为其不在 lambda 中因此不必是 final
        System.out.println(atomicInteger.getAndAccumulate(10, (p, x) -> p + x));

        // 计算并获取（i = 10, p 为 i 的当前值, x 为参数1, 结果 i = 0, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(atomicInteger.accumulateAndGet(-10, (p, x) -> p + x));

        AtomicInteger i = new AtomicInteger(5);
        myUpdateAndGet(i, operand -> operand / 2);
        System.out.println("i = " + i.get());

    }

    public static void myUpdateAndGet(AtomicInteger oldValue, IntUnaryOperator operator) {
        while (true) {
            int expectValue = oldValue.get();
            int newValue = operator.applyAsInt(expectValue);
            if (oldValue.compareAndSet(expectValue, newValue)) {
                break;
            }
        }
    }

}
