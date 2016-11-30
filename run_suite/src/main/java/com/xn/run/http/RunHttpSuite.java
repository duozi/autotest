package com.xn.run.http;/**
 * Created by xn056839 on 2016/10/28.
 */

import com.xn.common.model.Suite;
import com.xn.common.result.HTMLReport;
import com.xn.common.result.Report;
import com.xn.common.service.GetPara;
import com.xn.common.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunHttpSuite {
    private static final Logger logger = LoggerFactory.getLogger(RunHttpSuite.class);
    public static ExecutorService exe = Executors.newFixedThreadPool(50);
    public static boolean httpUseSign=true;
    public static String path = "/data/autotest/user/";
    public static String sendMailTo = "zhouxi2@xiaoniu66.com";

    public void setTestSites(List<Suite> testSites) {
        this.testSuites = testSites;
    }

    List<Suite> testSuites;
    public void run() throws InterruptedException {
        Report.getReport().setStartTime(new Date());
        boolean falg = DBUtil.DBInit();
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

                if (falg) {
                    DBUtil.DBClose();
                }
//                Report.getReport().generateReport();
                HTMLReport.generateResultReport(path, sendMailTo);
                break;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
//        if (args.length < 2) {
//            logger.error("输入参数错误：[依赖jar地址] [测试报告邮件接受人]");
//            return;
//        }
path="";

GetPara getPara=new GetPara();
        getPara.setPath(path);
//        loader=ReflectionUtils.addJar("d:/user-interface-2.0.0-20160926.085005-12.jar");
//        sendMailTo = args[1];
        sendMailTo="zhouxi2@xiaoniu66.com";

        ReadHttpSuite readHttpSuite = new ReadHttpSuite();
        RunHttpSuite runHttpSuite = new RunHttpSuite();
        List<Suite> suites = readHttpSuite.getSuites(path);
        runHttpSuite.setTestSites(suites);
        runHttpSuite.run();

//        logger.info("执行报告的地址在 /data/autotest/user/report");
    }
}
