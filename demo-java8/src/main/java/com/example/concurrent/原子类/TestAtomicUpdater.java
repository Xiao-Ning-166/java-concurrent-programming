package com.example.concurrent.原子类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@Slf4j(topic = "log.atomicUpdater")
public class TestAtomicUpdater {

    public static void main(String[] args) {
        AtomicReferenceFieldUpdater<Student, String> stringAtomicReferenceFieldUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        Student student = new Student();

        System.out.println(stringAtomicReferenceFieldUpdater.compareAndSet(student, null, "张三"));
        System.out.println(student);
    }

}

class Student {

    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
