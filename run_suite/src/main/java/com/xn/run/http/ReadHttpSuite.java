package com.xn.run.http;/**
 * Created by xn056839 on 2016/10/28.
 */


import com.xn.common.Exception.CaseErrorEqualException;
import com.xn.common.command.AssertCommand;
import com.xn.common.command.Command;
import com.xn.common.command.HttpCaseCommand;
import com.xn.common.command.TestCaseCommand;
import com.xn.common.model.Suite;
import com.xn.common.result.Report;
import com.xn.common.service.PaserFile;
import com.xn.common.util.FileUtil;
import com.xn.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import static com.xn.common.util.SignUtil.httpAddSign;


public class ReadHttpSuite {
    private static final Logger logger = LoggerFactory.getLogger(ReadHttpSuite.class);
    public PaserFile paser = new PaserFile();
    private Suite suite;
    List<Suite> suites = new ArrayList();
    String interfaceName;
    String url;
    String timeout;
    String useSign;
    String signType;
    String requestType;
    String paramType;
    String signAddSignType;//计算key是否加signType;默认是不加


    public static int totalCase = 0;
    public static String path = "";

    public List<Suite> getSuites(String path) {
        readSuitFile(path);
        return suites;
    }

    public void readSuitFile(String path) {

        File folder = new File(path + "suite/http/");
        if (!folder.exists()) {
            return;
        }
        File[] interfaces = folder.listFiles();
        if (interfaces.length == 0) {
            return;
        }

        //接口名层
        for (File interfaceFolder : interfaces) {

            if (!interfaceFolder.getName().startsWith("#") && !interfaceFolder.getName().startsWith(".") && interfaceFolder.isDirectory()) {
                interfaceName = interfaceFolder.getName();
                File[] cases = interfaceFolder.listFiles();
                //cese名层
                int jumpMethod = 1;//跳过service 1代表跳过
                List<Command> testCaseCommandList=new ArrayList();
                suite = new Suite();
                for (File caseFile : cases) {

                    //配置文件和case

                    if (!caseFile.getName().startsWith("#")) {
                        if (caseFile.getName().equals("serviceConfig.properties")) {
                            url = StringUtil.getConfig(caseFile, "url", "");
                            timeout = StringUtil.getConfig(caseFile, "timeout", "200000");
                            //这个参数的逻辑是true代表访问这个接口需要计算签名，如果sign不为空，则由框架计算签名，如果sign不为空，则使用传入的签名
                            useSign=StringUtil.getConfig(caseFile,"useSign","false");
                            signType=StringUtil.getConfig(caseFile,"signType","");
                            requestType=StringUtil.getConfig(caseFile,"requestType","POST");
                            paramType=StringUtil.getConfig(caseFile,"paramType","form");
                            signAddSignType=StringUtil.getConfig(caseFile,"addSignType","false");
                            break;
                        }
                    }
                }

                for (File caseFile : cases) {

                    //配置文件和case

                    if (!caseFile.getName().startsWith("#")) {
                        if (caseFile.getName().equals("serviceConfig.properties")) {

                            continue;
                        } else if (caseFile.getName().equals("beforeClass")) {
                            suite.setBeforeClass(paser.dealBeforeClassFile(caseFile));
                        } else if (caseFile.getName().equals("afterClass")) {
                            suite.setAfterClass(paser.dealAfterClassFile(caseFile));
                        } else if (caseFile.isDirectory()) {
                            String caseName = caseFile.getName();

                                int jump = 0;//跳过case,1代表跳过
                                File[] fs = caseFile.listFiles();

                                TestCaseCommand testCaseCommand = new TestCaseCommand();

                                String casePath = caseFile.getPath() + "/log";
                                for (File f : fs) {
                                    if (f.getName().equals("before")) {
                                        testCaseCommand.setBeforeCommand(paser.dealFile(f));
                                    } else if (f.getName().equals("after")) {
                                        testCaseCommand.setAfterCommand(paser.dealFile(f));
                                    } else if (f.getName().equals("assert")) {

                                        testCaseCommand.setAssertCommand((AssertCommand) paser.dealAssertFile(f, "#"+caseName, interfaceName, caseName));
                                    } else if (!f.getName().equals("log")) {
                                        try {
                                            testCaseCommand.setCaseCommand(dealCaseFile(f, casePath));
                                            jumpMethod = 0;
                                        } catch (CaseErrorEqualException e) {
                                            logger.error("jump this case {}", interfaceName + "/" + caseName + "/" + caseName);
                                            jump = 1;

                                        }


                                    }

                                }
                                if (jump == 0) {
                                    testCaseCommandList.add(testCaseCommand);
                                }

                        }




                    } else {
                        if(caseFile.isDirectory()){
                        logger.error("jump this case {}", interfaceName + "/" + caseFile.getName());}
                    }
                }
                if (jumpMethod == 0 ) {
                    suite.setTestCase(testCaseCommandList);

                }
                if (suite.getTestCase() != null) {

                    suites.add(suite);
                }


            }
        }

        Report.getReport().setTotal(totalCase);
    }

    private HttpCaseCommand dealCaseFile(File f, String casePath) throws CaseErrorEqualException {
        String content = FileUtil.fileReadeForStr(f);
        JSONObject json = JSONObject.fromObject(content);

        Iterator<String> keyItr = json.keys();
        String key;
        TreeMap<String, String> outMap = new TreeMap();
        while (keyItr.hasNext()) {
            key = keyItr.next();
            outMap.put(key, json.getString(key));
        }

        String para = httpAddSign(outMap, Boolean.parseBoolean(useSign),signType,paramType,signAddSignType);

        if (para.length() > 0)
            totalCase++;
        return new HttpCaseCommand(casePath, para, url, timeout,requestType,paramType);
    }

    public static void main(String[] args) {
//        ReadHttpSuite readHttpSuite = new ReadHttpSuite();
//        readHttpSuite.readSuitFile();
    }
}
