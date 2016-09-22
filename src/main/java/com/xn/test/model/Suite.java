package com.xn.test.model;
/**
 * Created by xn056839 on 2016/9/2.
 */

import com.xn.test.command.Command;
import com.xn.test.command.TestCaseCommand;
import com.xn.test.result.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Suite {
    private static final Logger logger = LoggerFactory.getLogger(Suite.class);
    public static ExecutorService exe = Executors.newFixedThreadPool(80);
    List<Command> beforeClass;
    List<Command> afterClass;
    List<Command> testCase;
//    public static int size = 0;

//    private synchronized static void addSize() {
//        size = size + 1;
//    }

    public static Logger getLogger() {
        return logger;
    }

    public List<Command> getBeforeClass() {
        return beforeClass;
    }

    public void setBeforeClass(List<Command> beforeClass) {
        this.beforeClass = beforeClass;
    }


    public List<Command> getAfterClass() {
        return afterClass;
    }

    public void setAfterClass(List<Command> afterClass) {
        this.afterClass = afterClass;
    }


    public List<Command> getTestCase() {
        return testCase;
    }

    public void setTestCase(List<Command> testCase) {
        this.testCase = testCase;
    }

    public Suite(List<Command> beforeClass, List<Command> afterClass, List<Command> testCase) {
        this.beforeClass = beforeClass;

        this.afterClass = afterClass;


        this.testCase = testCase;
    }

    public Suite() {
    }


    public void execute() {
        if (beforeClass != null) {
            beforeClass.forEach(Command::execute);
            if (testCase != null) {
                for (int i = 0; i < testCase.size(); i++) {
                    int finalI = i;
                    exe.execute(new Runnable() {
                                    @Override
                                    public void run() {
//                                        addSize();
                                        testCase.get(finalI).execute();
                                    }
                                }
                    );

                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }

                exe.shutdown();
                while (true) {
//                    if(size==Report.getReport().getTotal()){
//
//                    }

                    if (exe.isTerminated()) {
                        System.out.println("-----------");
                        if (afterClass != null) {
                            afterClass.forEach(Command::execute);
                        }
                        Report.getReport().setStopTime(new Date());
                        break;
                    }

                }

            }

        }
    }
}