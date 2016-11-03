package com.xn.dubbo;/**
 * Created by xn056839 on 2016/9/2.
 */

import com.xn.common.Exception.CaseErrorEqualException;
import com.xn.common.command.AssertCommand;
import com.xn.common.command.Command;
import com.xn.common.command.DubboCaseCommand;
import com.xn.common.command.TestCaseCommand;
import com.xn.common.model.KeyValueStore;
import com.xn.common.model.ServiceDesc;
import com.xn.common.model.Suite;
import com.xn.common.result.Report;
import com.xn.common.service.PaserFile;
import com.xn.common.util.FileUtil;
import com.xn.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xn.dubbo.RunDubboSuite.loader;

public class ReadDubboSuite {
    private static final Logger logger = LoggerFactory.getLogger(ReadDubboSuite.class);
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private Suite suite;
    private List<Suite> suites = new ArrayList();
    private ServiceDesc serviceDesc;


    public static int totalCase = 0;
    public PaserFile paser = new PaserFile();

    public List<Suite> getSuites(String path) {
        readSuitFile(path);
        return suites;
    }

    String interfaceName;
    String methodName;
    String url;
    String appName;
    String timeout;
    String version;
    String group;

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public void readSuitFile(String path) {

        File folder = new File(path + "suite/dubbo/");
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
                File[] methods = interfaceFolder.listFiles();
                //方法名层
                for (File methodFolder : methods) {

                    if (methodFolder.getName().equals("serviceConfig.properties")) {
                        url = StringUtil.getConfig(methodFolder, "url", "");
                        appName = StringUtil.getConfig(methodFolder, "appName", "");
                        timeout = StringUtil.getConfig(methodFolder, "timeout", "0");
                        version = StringUtil.getConfig(methodFolder, "version", "1.0");
                        group = StringUtil.getConfig(methodFolder, "group", "");
                        break;
                    }

                }
                for (File methodFolder : methods) {
                    if (!methodFolder.getName().startsWith("#")) {
                        if (methodFolder.getName().equals("serviceConfig.properties")) {
                            continue;
                        }

                        int jumpMethod = 1;//跳过service 1代表跳过
                        File[] files = methodFolder.listFiles();
                        //配置文件和case
                        List<Command> testCaseCommandList = new ArrayList();
                        suite = new Suite();
                        for (File file : files) {
                            if (file.getName().equals("config.properties")) {
                                interfaceName = StringUtil.getConfig(file, "interfaceName", "");
                                methodName = StringUtil.getConfig(file, "methodName", "");
                                this.serviceDesc = new ServiceDesc(interfaceName, methodName, url, version, group, timeout, appName);
                                break;
                            }
                        }
                        for (File file : files) {

                            if (file.getName().equals("config.properties")) {
                                continue;
                            } else if (file.getName().equals("beforeClass")) {
                                suite.setBeforeClass(paser.dealBeforeClassFile(file));
                            } else if (file.getName().equals("afterClass")) {
                                suite.setAfterClass(paser.dealAfterClassFile(file));
                            } else if (file.isDirectory()) {
                                String caseName = file.getName();
                                if (!caseName.startsWith("#")) {
                                    int jump = 0;//跳过case,1代表跳过
                                    File[] fs = file.listFiles();

                                    TestCaseCommand testCaseCommand = new TestCaseCommand();

                                    String casePath = file.getPath() + "/log";
                                    for (File f : fs) {
                                        if (f.getName().equals("before")) {
                                            testCaseCommand.setBeforeCommand(paser.dealFile(f));
                                        } else if (f.getName().equals("after")) {
                                            testCaseCommand.setAfterCommand(paser.dealFile(f));
                                        } else if (f.getName().equals("assert")) {

                                            testCaseCommand.setAssertCommand((AssertCommand) paser.dealAssertFile(f, caseName, serviceDesc.getClazz(), serviceDesc.getMethodName()));
                                        } else if (!f.getName().equals("log")) {
                                            try {
                                                testCaseCommand.setCaseCommand( dealCaseFile(f, casePath));
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
                                } else {
                                    logger.error("jump this case {}", interfaceName + "/" + methodName + "/" + caseName);
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
        }
        Report.getReport().setTotal(totalCase);
        Report.getReport().setDubboTotal(totalCase);


    }

    /**
     * 处理参数case文件
     *
     * @param file
     * @param casePath
     * @return
     * @throws CaseErrorEqualException
     */
    public DubboCaseCommand dealCaseFile(File file, String casePath) throws CaseErrorEqualException {
        List<KeyValueStore> list = new ArrayList();
        List<String> lines = FileUtil.fileReadeForList(file);
        lines = StringUtil.dubboAddSign(lines);
        for (String line : lines) {
            if (!line.startsWith("#") & line.contains("=") && line.split("=").length == 2) {
                String type = line.split("=")[0];
                String value = line.split("=")[1];
                KeyValueStore keyValueStore = new KeyValueStore(type, value);
                list.add(keyValueStore);
            }
        }
        if (list.size() > 0)
            totalCase++;
        return new DubboCaseCommand(list, serviceDesc, casePath, loader);
    }

    public static void main(String[] args) {
//        ReadDubboSuite re = new ReadDubboSuite();
//        re.readSuitFile();
//        dealFile("suite");
//        dealDBFile("suite");
    }

}
