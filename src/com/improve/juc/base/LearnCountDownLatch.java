package com.improve.juc.base;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhengxianyou on 2019/4/14.
 * <p>
 * CountDownLatch ：闭锁，在完成某些运算是，只有其他所有线程的运算全部完成，当前运算才继续执行
 */
public class LearnCountDownLatch {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);//参数代表将要开启的线程数,即等待多少个线程之后才继续执行
        LatchDemo ld = new LatchDemo(latch);
        long s = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(ld).start();
        }

        try {
            latch.await();//等待其他所有线程的运算全部完成，才继续执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long e = System.currentTimeMillis();
        System.out.println("执行耗时"+(e-s));
    }
}

class LatchDemo implements Runnable {
    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3000; i++) {
                if (i % 103 == 0)
                    System.out.println(i);
            }
        } finally {
            latch.countDown();
        }
    }
}