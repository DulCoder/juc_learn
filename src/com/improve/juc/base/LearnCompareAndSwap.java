package com.improve.juc.base;

import java.util.Random;

/**
 * Created by zhengxianyou on 2019/4/14.
 *
 * 模拟CAS算法
 */
public class LearnCompareAndSwap {

    public static void main(String[] args) {
        CompareAndSwap cas = new CompareAndSwap();

        for (int i = 0; i < 10; i++) {
          new Thread(() -> {
              int expectedValue = cas.getValue();
              boolean b = cas.compareAndSet(expectedValue, new Random().nextInt(100));
              System.out.println(b);
          }).start();
        }
    }
}

class CompareAndSwap{
    private int value;

    //获取内存值
    public synchronized int getValue() {
        return value;
    }

    public synchronized int compareAndSwap(int expectedValue, int newValue){
        int oldValue = value;
        if (oldValue==expectedValue)
            this.value=newValue;

        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue){
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
