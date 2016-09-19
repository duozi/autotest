package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.test.model.Suite;
import com.xn.test.result.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunTestSuite {
    private static final Logger logger = LoggerFactory.getLogger(RunTestSuite.class);
    public static ExecutorService exe = Executors.newFixedThreadPool(50);

    public void setTestSites(List<Suite> testSites) {
        this.testSuites = testSites;
    }

    List<Suite> testSuites;

    public void run() throws InterruptedException {
        Report.getReport().setStartTime(new Date());
        for (int i = 0; i < testSuites.size(); i++) {

            int finalI = i;
            exe.execute(new Runnable() {
                @Override
                public void run() {
                    testSuites.get(finalI).execute();
                }
            });

        }
        exe.shutdownNow();
        while (true) {
            if (exe.isTerminated()) {
                Report.getReport().setStopTime(new Date());

                int j = Report.getReport().getFailed();
                logger.warn("---------" + j);
                Report.getReport().generateReport();
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadSuite readSuite = new ReadSuite();
        RunTestSuite runTestSuite = new RunTestSuite();
        List<Suite> suites = readSuite.getSuites();
        runTestSuite.setTestSites(suites);
        runTestSuite.run();

    }
}
