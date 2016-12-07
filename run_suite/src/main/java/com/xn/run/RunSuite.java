package com.xn.run;/**
 * Created by xn056839 on 2016/11/2.
 */

import com.xn.common.model.Suite;
import com.xn.common.result.HTMLReport;
import com.xn.common.result.Report;
import com.xn.common.service.GetPara;
import com.xn.common.util.DBUtil;
import com.xn.common.util.JarUtil;
import com.xn.run.dubbo.ReadDubboSuite;
import com.xn.run.http.ReadHttpSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunSuite {
    private static final Logger logger = LoggerFactory.getLogger(RunSuite.class);
    public static ExecutorService exe = Executors.newFixedThreadPool(50);


    /**
     * @param suitePath 不包括suite那一层
     * @param jarPath   不包括具体的jar名
     */
    public void runSuiteService(String type,String system, String sendMailTo, String suitePath, String jarPath) {

        File dubbo = new File(suitePath + "suite/dubbo/");
        File http = new File(suitePath + "suite/http/");
        GetPara getPara = new GetPara();
        getPara.setPath(suitePath);
        getPara.setSystem(system);
        Report.getReport().setStartTime(new Date());

        boolean falg  = DBUtil.DBInit();

        if (type.equals("dubbo") && dubbo.exists() && dubbo.isDirectory()) {
            JarUtil.addJar(jarPath);
            ReadDubboSuite readDubboSuite = new ReadDubboSuite();
            List<Suite> dubboTestSuites = readDubboSuite.getSuites(suitePath);
            try {
                run(dubboTestSuites);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (type.equals("http") && http.exists() && http.isDirectory()) {

            ReadHttpSuite readHttpSuite = new ReadHttpSuite();
            List<Suite> httpTestSuites = readHttpSuite.getSuites(suitePath);
            try {
                run(httpTestSuites);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exe.shutdownNow();
        while (true) {
            if (exe.isTerminated()) {
                Report.getReport().setStopTime(new Date());

         if (falg) {
                    DBUtil.DBClose();
                }
                HTMLReport.generateResultReport(suitePath, sendMailTo);
                break;
            }
        }
    }

    public void run(final List<Suite> testSuites) throws InterruptedException {

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

    }

    //一般有四个参数 第一个是type的类型，第二个是报告的邮件接收者，第三个是suite文件地址，第四个是jar文件的地址
    public static void main(String[] args) {
        String type = args[0];
        String system=args[1];
        String sendMailTo = args[2];
        String suitePath = args[3];
        String jarPath = args[4];
        RunSuite runSuite = new RunSuite();
        jarPath=jarPath+"/"+system+"/";
        runSuite.runSuiteService(type, system,sendMailTo, suitePath, jarPath);
    }
}
