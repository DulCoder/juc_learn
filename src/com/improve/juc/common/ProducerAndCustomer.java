package com.improve.juc.common;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengxianyou on 2019/4/16.
 *
 * 生产者消费者案例
 *
 * 虚假唤醒(spurious wakeup)
 * 一般而言线程调用await()方法后，需要其他线程调用signal(),signalAll()方法后，线程才会从await()方法中返回，
 * 而虚假唤醒(spurious wakeup)是指线程通过其他方式，从await()方法中返回。
 *
 * 因为wait方法可以分为三个操作：
 * （1）释放锁并阻塞
 * （2）等待条件cond发生
 * （3）获取通知后，竞争获取锁
 *
 * 例如：同时有消费者A、B消费商品，线程A调用wait方法进入等待队列，线程B买票时发现生产者线程在生产商品，
 * 获取锁失败，线程B阻塞，进入阻塞队列，生产者线程生产商品时，产品数量+1（满足条件2 等待条件发生），
 * 生产者线程调用signalAll()方法后，线程B马上竞争获取到锁，消费成功后产品数量为0，
 * 而线程A此时正处于await()方法醒来过程中的第三步（竞争获取锁获取锁），
 * 当线程B释放锁，线程A获取锁后，会执行消费的操作，而此时是没有产品的。
 *
 */
public class ProducerAndCustomer {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        new Thread(new Producer(clerk),"生产者").start();
        new Thread(new Customer(clerk),"消费者A").start();
        new Thread(new Customer(clerk),"消费者B").start();

    }
}

/**
 * 负责销售和进货的店员
 */
class Clerk {
    private int product = 50;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void buy() {
        lock.lock();

        try {
            if (product > 49) { // 为了避免虚假唤醒，应该总是使用在循环中。但是while(true)循环CPU资源消耗大
                System.out.println("啊哦!!!,产品已满----------");

                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                System.out.println(Thread.currentThread().getName() + ": " + (++product));
                condition.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public void sale(int num) {
        lock.lock();

        try {
            if (product < num) {
                System.out.println("啊哦!!!,产品数量不足----------");

                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                product -= num;
                System.out.println(Thread.currentThread().getName() + ": " + product);
                condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}


/**
 * 生产者
 */
class Producer implements Runnable{
    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 200; i++) {
            clerk.buy();
        }
    }
}

/**
 * 消费者
 */
class Customer implements Runnable{
    private Clerk clerk;

    public Customer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
//            int n = new Random().nextInt(6)+1;
            clerk.sale(1);
        }
    }
}