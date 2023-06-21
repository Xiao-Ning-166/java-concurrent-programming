package com.example.concurrent.原子类;

import jdk.internal.misc.Unsafe;

import java.math.BigDecimal;

public class TestMyAtomicInteger {

    public static void main(String[] args) {
        DecimalAccount decimalAccount = new MyAccount(10000);
        DecimalAccount.demo(decimalAccount);
    }

}

class MyAccount implements DecimalAccount {

    private MyAtomicInteger myAtomicInteger;

    public MyAccount(int account) {
        this.myAtomicInteger = new MyAtomicInteger(account);
    }

    @Override
    public BigDecimal getBalance() {
        return new BigDecimal(myAtomicInteger.getValue());
    }

    @Override
    public void withdraw(BigDecimal amount) {
        myAtomicInteger.decrement(amount.intValue());
    }
}

class MyAtomicInteger {

    private volatile int value;

    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    private static final long VALUE_OFFSET = UNSAFE.objectFieldOffset(MyAtomicInteger.class, "value");

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while (true) {
            int oldVale = getValue();
            int newVale = oldVale - amount;
            if (UNSAFE.compareAndSetInt(this, VALUE_OFFSET, oldVale, newVale)) {
                break;
            }
        }
    }
}
