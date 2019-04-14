package com.improve.juc.common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengxianyou on 2019/4/14.
 * <p>
 * 一、用于解决多线程安全问题的方式：
 * <p>
 * synchronized:隐式锁
 * 1. 同步代码块
 * <p>
 * 2. 同步方法
 * <p>
 * jdk 1.5 后：
 * 3. 同步锁 Lock
 * 注意：是一个显示锁，需要通过 lock() 方法上锁，必须通过 unlock() 方法进行释放锁
 */
public class LearnLock {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(ticket, "搜票窗口一").start();
        new Thread(ticket, "搜票窗口二").start();
        new Thread(ticket, "搜票窗口三").start();
    }
}

class Ticket implements Runnable {
    private int ticket = 30;
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                if (ticket > 0) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + " 余票：" + (--ticket));
                }else {
                    break;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}