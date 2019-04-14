package com.improve.juc.common;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhengxianyou on 2019/4/14.
 * <p>
 * 1. ReadWriteLock : 读写锁
 * <p>
 * 写写/读写 需要“互斥”
 * 读读 不需要互斥
 */
public class LearnReadWriteLock {

    public static void main(String[] args) {
        ReadWriteLockDemo rw = new ReadWriteLockDemo();

        new Thread(() -> rw.set(new Random().nextInt(133)), "write").start();

        for (int i = 0; i < 100; i++)
            new Thread(() -> rw.get()).start();
    }

}


class ReadWriteLockDemo {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private int number = 0;

    public void get() {
        lock.readLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + " : " + number);
        } finally {
            lock.readLock().unlock(); //释放锁
        }
    }

    public void set(int value) {
        lock.writeLock().lock();

        try {
            System.out.println(Thread.currentThread().getName()+": "+value);
            this.number = value;
        } finally {
            lock.writeLock().unlock();
        }
    }

}