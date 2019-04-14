package com.improve.juc.common;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengxianyou on 2019/4/14.
 * <p>
 * 笔试题：
 * 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印 10 遍，
 * 要求输出的结果必须按顺序显示。
 * 如：ABCABCABC…… 依次递归
 *
 * Condition： 控制线程通信
 *
 * 1、Condition 接口描述了可能会与锁有关联的条件变量。这些变量在用
 * 法上与使用 Object.wait 访问的隐式监视器类似，但提供了更强大的
 * 功能。需要特别指出的是，单个 Lock 可能与多个 Condition 对象关
 * 联。为了避免兼容性问题， Condition 方法的名称与对应的 Object 版本中的不同。
 * 2、在 Condition 对象中，与 wait、 notify 和 notifyAll 方法对应的分别是
 * await、 signal 和 signalAll。
 * 3、Condition 实例实质上被绑定到一个锁上。要为特定 Lock 实例获得
 * Condition 实例，请使用其 newCondition() 方法
 */
public class LearnABCAlternate {
    public static void main(String[] args) {
        AlternateDemo ad = new AlternateDemo();

        ABCAlternate a = new ABCAlternate(1,ad);
        ABCAlternate b = new ABCAlternate(2,ad);
        ABCAlternate c = new ABCAlternate(3,ad);

            new Thread(a,"A").start();
            new Thread(b,"B").start();
            new Thread(c,"C").start();

    }
}

class ABCAlternate implements Runnable {
    private int flag;
    private AlternateDemo ad;

    public ABCAlternate(int flag, AlternateDemo ad) {
        this.flag = flag;
        this.ad = ad;
    }

    @Override
    public void run() {
        switch (flag) {
            case 1:
                for (int i = 1; i < 11; i++)
                ad.loopA(i);
                break;
            case 2:
                for (int i = 1; i < 11; i++)
                    ad.loopB(i);
                break;
            case 3:
                for (int i = 1; i < 11; i++)
                    ad.loopC(i);
                break;
        }
    }
}

class AlternateDemo {
    private int number = 1; //当前正在执行线程的标记

    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();


    public void loopA(int total) {
        lock.lock();
        try {
            if (number != 1)
                condition1.await();
            System.out.println(Thread.currentThread().getName()+ "第 "+total+" 轮");

            number = 2;
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int total) {
        lock.lock();
        try {
            if (number != 2)
                condition2.await();
            System.out.println(Thread.currentThread().getName()+ "第 "+total+" 轮");

            number = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int total) {
        lock.lock();
        try {
            if (number != 3)
                condition3.await();
            System.out.println(Thread.currentThread().getName()+ "第 "+total+" 轮");

            number = 1;
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}