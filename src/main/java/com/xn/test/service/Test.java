package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/8.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test extends Thread{
    private final int i;
    public Test(int i){
        this.i = i;
    }
    @Override
    public void run(){
        System.out.println(i);
        for(int j=0;j<10;j++){
            System.out.println(i+"--------"+j);

        }

    }
    public static void main(String args[]) throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(50);
        for (int i = 1; i <= 5; i++) {

            exe.execute(new Test(i));
        }
        exe.shutdown();
        while (true) {
            if (exe.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(200);
        }
    }
}
