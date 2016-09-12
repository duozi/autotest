package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/2.
 */

import com.xn.test.Exception.CaseErrorEqualException;
import com.xn.test.command.*;
import com.xn.test.response.Assert;
import com.xn.test.model.KeyValueStore;
import com.xn.test.model.ServiceDesc;
import com.xn.test.model.Suite;
import com.xn.test.result.Report;
import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadSuite {
    private static final Logger logger = LoggerFactory.getLogger(ReadSuite.class);
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private Suite suite;
    List<Suite> suites = new ArrayList<>();
    private ServiceDesc serviceDesc;
    String interfaceName;
    public CreateCommand createCommand = new CreateCommand();
    public static int totalCase = 0;


    public List<Suite> getSuites() {
        readSuitFile();
        return suites;
    }


    public ServiceDesc getServiceDesc() {
        return serviceDesc;
    }


    String methodName;
    String url;
    String appName;
    String timeout;
    String version;
    String group;

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public void readSuitFile() {

        File folder = new File("suite");
        File[] interfaces = folder.listFiles();
        //接口名层
        for (File interfaceFolder : interfaces) {

            if (interfaceFolder.isDirectory()) {
                File[] methods = interfaceFolder.listFiles();
                //方法名层
                for (File methodFolder : methods) {
                    int jumpMethod = 1;//跳过service 1代表跳过
                    File[] files = methodFolder.listFiles();
                    //配置文件和case
                    List<Command> testCaseCommandList = new ArrayList<>();
                    suite = new Suite();
                    for (File file : files) {

                        if (file.getName().equals("config.properties")) {
                            interfaceName = StringUtil.getConfig(file, "interfaceName", "");
                            methodName = StringUtil.getConfig(file, "methodName", "");
                            url = StringUtil.getConfig(file, "url", "");
                            appName = StringUtil.getConfig(file, "appName", "");
                            timeout = StringUtil.getConfig(file, "timeout", "0");
                            version = StringUtil.getConfig(file, "version", "1.0");
                            group = StringUtil.getConfig(file, "group", "");

                            this.serviceDesc = new ServiceDesc(interfaceName, methodName, url, version, group, timeout, appName);
                        } else if (file.getName().equals("beforeClass")) {
                            suite.setBeforeClass(dealBeforeClassFile(file));
                        } else if (file.getName().equals("afterClass")) {
                            suite.setAfterClass(dealAfterClassFile(file));
                        } else if (file.isDirectory()) {
                            int jump = 0;//跳过case,1代表跳过
                            File[] fs = file.listFiles();

                            TestCaseCommand testCaseCommand = new TestCaseCommand();
                            String caseName = file.getName();
                            String casePath = file.getPath() + "/log";
                            for (File f : fs) {
                                if (f.getName().equals("before")) {
                                    testCaseCommand.setBeforeCommand(dealFile(f));
                                } else if (f.getName().equals("after")) {
                                    testCaseCommand.setAfterCommand(dealFile(f));
                                } else if (f.getName().equals("assert")) {

                                    testCaseCommand.setAssertCommand((AssertCommand) dealAssertFile(f, caseName));
                                } else if (!f.getName().equals("log")) {
                                    try {
                                        testCaseCommand.setCaseCommand((CaseCommand) dealCaseFile(f, casePath));
                                        jumpMethod = 0;
                                    } catch (CaseErrorEqualException e) {
                                        logger.error("jump this case {}", interfaceName + "/" + methodName + "/" + caseName);
                                        jump = 1;

                                    }


                                }

                            }
                            if (jump == 0) {
                                testCaseCommandList.add(testCaseCommand);
                            }
                        }

                    }
                    if (jumpMethod == 0) {
                        suite.setTestCase(testCaseCommandList);

                    }
                    if (suite.getTestCase() != null) {

                        suites.add(suite);
                    }


                }


            }
        }
        Report.getReport().setTotal(totalCase);
    }

    public Command dealCaseFile(File file, String casePath) throws CaseErrorEqualException {
        List<KeyValueStore> list = new ArrayList<>();
        List<String> lines = FileUtil.fileReadeForList(file);
        lines = StringUtil.listAddSign(lines);
        for (String line : lines) {
            if (!line.startsWith("#")&line.contains("=") && line.split("=").length == 2) {
                String type = line.split("=")[0];
                String value = line.split("=")[1];
                KeyValueStore keyValueStore = new KeyValueStore(type, value);
                list.add(keyValueStore);
            }
        }
        if (list.size() > 0)
            totalCase++;
        return createCommand.createCaseCommand(list, serviceDesc, casePath);
    }

    public Command dealAssertFile(File file, String caseName) {
        List<KeyValueStore> list = new ArrayList<>();
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.contains("=") && line.split("=").length == 2) {
                String type = line.split("=")[0];
                String value = line.split("=")[1];
                KeyValueStore keyValueStore = new KeyValueStore(type, value);
                list.add(keyValueStore);
            }
        }
        Assert assertItem = new Assert(serviceDesc.getClazz(), serviceDesc.getMethodName(), caseName);
        return createCommand.createAssertCommand(list, assertItem);
    }

    public List<Command> dealBeforeClassFile(File file) {

        List<Command> beforeClass = new ArrayList<>();
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.equalsIgnoreCase("DB")) {
                beforeClass.add(createCommand.createNewDBCommand());
            } else if (line.startsWith("DB")) {

                String sql = line.split("=", 2)[1];
                beforeClass.add(createCommand.createDBCommand(sql));
            }
        }
        return beforeClass;
    }

    public List<Command> dealAfterClassFile(File file) {

        List<Command> afterClass = new ArrayList<>();
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.equalsIgnoreCase("DB")) {
                afterClass.add(createCommand.createCloseDBCommand());
            } else if (line.startsWith("DB")) {

                String sql = line.split("=", 2)[1];
                afterClass.add(createCommand.createDBCommand(sql));
            }
        }
        return afterClass;
    }

    public List<Command> dealFile(File file) {
        List<Command> list = new ArrayList<>();

        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.startsWith("DB")) {
                String sql = line.split("=", 2)[1];
                list.add(createCommand.createDBCommand(sql));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        ReadSuite re = new ReadSuite();
        re.readSuitFile();
    }

}
