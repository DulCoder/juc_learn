package com.improve.juc.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhengxianyou on 2019/4/14.
 * 原子性变量
 * 一、i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
 * 		  int i = 10;
 * 		  i = i++; //10
 *
 * 		  int temp = i;
 * 		  i = i + 1;
 * 		  i = temp;
 *
 * 二、原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
 * 		1. volatile 保证内存可见性
 * 		2. CAS（Compare-And-Swap） 算法保证数据变量的原子性
 * 			CAS 算法是硬件对于并发操作的支持
 * 			CAS 包含了三个操作数：
 * 			①内存值  V
 * 			②预估值  A
 * 			③更新值  B
 * 			当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 *
 */
public class LearnAtomic {

    public static void main(String[] args) {
        AtomicDemo ad = new AtomicDemo();
        for (int i = 0; i < 15; i++) {
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable{
    //不能保证原子性，可能在修改未完成的时候其他线程也在同时修改并提交相同的结果，而不是想要的累加
//    	private volatile int serialNumber = 0;

    private AtomicInteger serialNumber = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(Thread.currentThread().getName()+": "+getSerialNumber()+" ");
    }

//    public int getSerialNumber() {
//        return serialNumber++;
//    }


    public int getSerialNumber() {
        return serialNumber.getAndIncrement();
    }
}
