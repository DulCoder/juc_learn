package com.improve.juc.base;

/**
 * Created by zhengxianyou on 2019/4/14.
 *
 * JUC简介：
 * 在 Java 5.0 提供了 java.util.concurrent （简称JUC ）包，在此包中增加了在并发编程中很常用
 * 的实用工具类， 用于定义类似于线程的自定义子系统，包括线程池、异步 IO 和轻量级任务框架。
 * 提供可调的、灵活的线程池。还提供了设计用于多线程上下文中的 Collection 实现等。
 *
 * 一、volatile 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据可见。
 * 					  相较于 synchronized 是一种较为轻量级的同步策略。
 *
 * 原理：程序执行时共享数据会被加载到“主存（堆）”中，JVM会为每一个线程分配独立的缓存，
 *       线程修改数据时会把堆内存中的数据读到自己的缓存中，修改完成之后再放回到堆内存中，
 *       在数据被一个线程修改未完成的时候另一个线程可能已经读取了堆中的数据到自己的缓存中，
 *       导致获取不到改变后的值，如果是while (true){}这样执行效率超高的循环，
 *       则线程就没有机会再次访问堆内存中的更新数据；
 *       以上是在正常多线程执行是内存不可见情况，使用volatile关键字修饰的共享数据可是使其处于内存可见状态
 *
 * 注意：
 * 1. volatile 不具备“互斥性” 即：不能保证同一时间只能有一个线程持有该数据
 * 2. volatile 不能保证变量的“原子性” 即：它所实现的能存可见性不是一步完成的；
 *    volatile实现内存可见性是通过store和load指令完成的；也就是对volatile变量执行写操作时，
 *    会在写操作后加入一条store指令，即强迫线程将最新的值刷新到主内存中；而在读操作时，
 *    会加入一条load指令，即强迫从主内存中读入变量的值。
 *    （原子性：int a=1; 是原子性操作；i++;不属于原子性操作，其实际操作过程为 int temp=i; i=i+1; i=temp;）
 *
 * synchronized和volatile的区别：
 * 1、执行效率 volatile > synchronized
 * 2、synchronized保证内存可见性和操作的原子性
 * 3、volatile只能保证内存可见性
 * 4、volatile不需要加锁，比Synchronized更轻量级，
 *    并不会阻塞线程（volatile不会造成线程的阻塞；synchronized可能会造成线程的阻塞。）
 * 5、volatile标记的变量不会被编译器优化,而synchronized标记的变量可以被编译器优化（如编译器重排序的优化）.
 * 6、volatile是变量修饰符，仅能用于变量，而synchronized是一个方法或块的修饰符。
 *    volatile本质是在告诉JVM当前变量在寄存器中的值是不确定的，使用前，需要先从主存中读取，
 *    因此可以实现可见性。而对n=n+1,n++等操作时，volatile关键字将失效，
 *    不能起到像synchronized一样的线程同步（原子性）的效果。
 *
 */
public class LearnVolatile {
    public static void main(String[] args) {
        VolatileDemo vd = new VolatileDemo();
        new Thread(vd).start();

        while (true){
            if (vd.isFlag()){
                System.out.println("-----终止循环-----");
                break;
            }
        }
    }
}


class VolatileDemo implements Runnable{
    private volatile boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200); //让main线程先执行，自己后执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;
        System.out.println("flag=" + isFlag());
    }

    public boolean isFlag() {
        return flag;
    }
}