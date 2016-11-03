package com.xn.run;/**
 * Created by xn056839 on 2016/11/2.
 */

import com.xn.common.model.Suite;
import com.xn.common.result.HTMLReport;
import com.xn.common.result.Report;
import com.xn.common.service.GetPara;
import com.xn.common.util.DBUtil;
import com.xn.common.util.JarUtil;
import com.xn.dubbo.ReadDubboSuite;
import com.xn.http.ReadHttpSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunSuite {
    private static final Logger logger = LoggerFactory.getLogger(RunSuite.class);
    public static boolean httpUseSign = true;
    public static String path = "/data/autotest/user/";
    public static String sendMailTo = "zhouxi2@xiaoniu66.com";
    public static ExecutorService exe = Executors.newFixedThreadPool(50);





    /**
     * @param suitePath 不包括suite那一层
     * @param jarPath   不包括具体的jar名
     */
    public void runSuiteService(String suitePath, String jarPath) {

        File dubbo = new File(suitePath + "suite/dubbo/");
        File http=new File(suitePath+"suite/http/");
        GetPara getPara=new GetPara();
        getPara.setPath(suitePath);
        Report.getReport().setStartTime(new Date());
        boolean falg = DBUtil.newDB();
        if (dubbo.exists() && dubbo.isDirectory()) {
            URLClassLoader loader= JarUtil.addJar(jarPath);
            getPara.setLoader(loader);

            ReadDubboSuite readDubboSuite=new ReadDubboSuite();
            List<Suite> dubboTestSuites=readDubboSuite.getSuites(suitePath);
            try {
                run(dubboTestSuites);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(http.exists()&&http.isDirectory()){

            ReadHttpSuite readHttpSuite=new ReadHttpSuite();
            List<Suite> httpTestSuites=readHttpSuite.getSuites(suitePath);
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
                    DBUtil.closeDB();
                }
//                Report.getReport().generateReport();
                HTMLReport.generateResultReport(path, sendMailTo);
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

    public static void main(String[] args) {
        String suitePath="";

        RunSuite runSuite=new RunSuite();
        runSuite.runSuiteService(suitePath,"d:/jar/");
    }
}
