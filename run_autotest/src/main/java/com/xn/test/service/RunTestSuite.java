package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.test.model.Suite;
import com.xn.test.result.Report;
import com.xn.test.util.DBUtil;
import com.xn.test.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.xn.test.result.HTMLReport.generateResultReport;

public class RunTestSuite {
    private static final Logger logger = LoggerFactory.getLogger(RunTestSuite.class);
    public static ExecutorService exe = Executors.newFixedThreadPool(50);
    public static String path = "/data/autotest/";
    public static URLClassLoader loader = null;
    public static String sendMailTo="zhouxi2@xiaoniu66.com";

    public void setTestSites(List<Suite> testSites) {
        this.testSuites = testSites;
    }

    List<Suite> testSuites;

    public void run() throws InterruptedException {
        Report.getReport().setStartTime(new Date());
        DBUtil.newDB();
        for (int i = 0; i < testSuites.size(); i++) {

            final int finalI = i;
            if (!exe.isShutdown()) {
                exe.execute(new Runnable() {
                    @Override
                    public void run() {
//                    System.out.println("1--------"+Thread.currentThread().getName());
                        testSuites.get(finalI).execute();
                    }
                });

            }
            try {
                Thread.sleep(50);

            } catch (InterruptedException e) {

            }

        }
        exe.shutdownNow();
        while (true) {
            if (exe.isTerminated()) {
                Report.getReport().setStopTime(new Date());

                DBUtil.closeDB();
//                Report.getReport().generateReport();
                generateResultReport();
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            logger.error("输入参数错误：[依赖jar地址] [测试报告邮件接受人]");
            return;
        }
//path="d:/";
        loader = ReflectionUtils.addJar(args[0]);
//        loader=ReflectionUtils.addJar("d:/user-interface-2.0.0-20160926.085005-12.jar");
        sendMailTo=args[1];
        ReadSuite readSuite = new ReadSuite();
        RunTestSuite runTestSuite = new RunTestSuite();
        List<Suite> suites = readSuite.getSuites();
        runTestSuite.setTestSites(suites);
        runTestSuite.run();

        logger.warn("执行报告的地址在 /data/autotest/report");
    }
}
