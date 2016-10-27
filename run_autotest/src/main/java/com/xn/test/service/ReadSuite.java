package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/2.
 */

import com.google.common.collect.Lists;
import com.xn.test.Exception.CaseErrorEqualException;
import com.xn.test.command.*;
import com.xn.test.model.KeyValueStore;
import com.xn.test.model.ServiceDesc;
import com.xn.test.model.Suite;
import com.xn.test.response.Assert;
import com.xn.test.result.Report;
import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xn.test.service.RunTestSuite.path;

public class ReadSuite {
    private static final Logger logger = LoggerFactory.getLogger(ReadSuite.class);
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private Suite suite;
    List<Suite> suites = new ArrayList();
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

        File folder = new File(path + "suite");
//        File folder = new File( "suite");
        File[] interfaces = folder.listFiles();
        //接口名层
        for (File interfaceFolder : interfaces) {

            if (!interfaceFolder.getName().startsWith("#")&&!interfaceFolder.getName().startsWith(".") && interfaceFolder.isDirectory()) {
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
                                suite.setBeforeClass(dealBeforeClassFile(file));
                            } else if (file.getName().equals("afterClass")) {
                                suite.setAfterClass(dealAfterClassFile(file));
                            } else if (file.isDirectory()) {
                                String caseName = file.getName();
                                if (!caseName.startsWith("#")) {
                                    int jump = 0;//跳过case,1代表跳过
                                    File[] fs = file.listFiles();

                                    TestCaseCommand testCaseCommand = new TestCaseCommand();

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
    }

    public Command dealCaseFile(File file, String casePath) throws CaseErrorEqualException {
        List<KeyValueStore> list = new ArrayList();
        List<String> lines = FileUtil.fileReadeForList(file);
        lines = StringUtil.listAddSign(lines);
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
        return createCommand.createCaseCommand(list, serviceDesc, casePath);
    }

    public Command dealAssertFile(File file, String caseName) {
        Assert assertItem = new Assert(serviceDesc.getClazz(), serviceDesc.getMethodName(), caseName);
        List<KeyValueStore> paralist = new ArrayList();
        List<KeyValueStore> redislist = new ArrayList();
        List<Command> redisAssertCommandList = new ArrayList();
        List<Command> dbAssertCommandList = new ArrayList();
        ParaAssertCommand paraAssertCommand=null;
        DBAssertCommand dbAssertCommand = null;
        List<String> lines = FileUtil.fileReadeForList(file);
        int redisFlag = 0;
        int dbFlag = 0;
        RedisAssertCommand redisAssertCommand = null;
        for (String line : lines) {
            if (!line.startsWith("#")) {
                if (redisFlag == 0) {
                    if (dbFlag == 0) {
                        if (line.contains("=") && line.split("=").length == 2) {
                            String type = line.split("=")[0];
                            String value = line.split("=")[1];
                            KeyValueStore keyValueStore = new KeyValueStore(type, value);
                            paralist.add(keyValueStore);
                        } else if (line.trim().equalsIgnoreCase("redis")) {
                            redisFlag = 1;
                            redisAssertCommand = new RedisAssertCommand();
                        } else if (line.trim().equalsIgnoreCase("DB")) {
                            dbFlag = 1;
                            dbAssertCommand = new DBAssertCommand();
                        }
                    } else if (dbFlag == 1) {
                        if (line.contains("=")) {
                            String type = line.split("=", 2)[0];
                            String value = line.split("=", 2)[1];
                            if (type.equalsIgnoreCase("sql")) {
                                dbAssertCommand.setSql(value);

                            } else if (type.equalsIgnoreCase("count")) {
                                dbAssertCommand.setExpectCount(value);
                            }
                        } else if (line.trim().equalsIgnoreCase("DB end")) {
                            dbFlag = 0;
                            dbAssertCommand.setAssertItem(assertItem);
                            dbAssertCommandList.add(dbAssertCommand);
                        }
                    }
                } else if (redisFlag == 1) {
                    if (line.trim().equalsIgnoreCase("redis end")) {
                        redisFlag = 0;
                        redisAssertCommand.setRedisParams(Lists.newArrayList(redislist));
                        redisAssertCommand.setAssertItem(assertItem);
                        redislist.clear();
                        redisAssertCommandList.add(redisAssertCommand);
                    } else if (line.contains("=") && line.split("=").length == 2) {
                        String key = line.split("=")[0];
                        String value = line.split("=")[1];
                        if (key.equalsIgnoreCase("redisMethod")) {
                            redisAssertCommand.setRedisMethod(value);

                        } else if (key.equalsIgnoreCase("key")) {
                            redisAssertCommand.setKey(value);
                        } else {
                            KeyValueStore keyValueStore = new KeyValueStore(key, value);
                            redislist.add(keyValueStore);
                        }
                    }
                }


            }

        }
        if (paralist.size() > 0) {
            paraAssertCommand = new ParaAssertCommand(paralist);
            paraAssertCommand.setAssertItem(assertItem);
        }
        return createCommand.createAssertCommand(paraAssertCommand, redisAssertCommandList, dbAssertCommandList, assertItem);

    }

    public List<Command> dealBeforeClassFile(File file) {

        List<Command> beforeClass = new ArrayList();
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

        List<Command> afterClass = new ArrayList();
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
        List<Command> list = new ArrayList();

        List<String> lines = FileUtil.fileReadeForList(file);
        RedisCommand redisCommand = null;
        int redisFlag = 0;
        for (String line : lines) {
            if (redisFlag == 0 && line.startsWith("DB")) {
                String sql = line.split("=", 2)[1];
                list.add(createCommand.createDBCommand(sql));
            } else if (line.trim().equalsIgnoreCase("redis")) {
                redisFlag = 1;
                redisCommand = new RedisCommand();
            } else if (redisFlag == 1) {

                if (line.startsWith("redisMethod")) {
                    if (line.split("=").length == 2) {
                        redisCommand.setMethodName(line.split("=")[1]);
                    }

                } else if (line.trim().equalsIgnoreCase("redis end")) {
                    redisFlag = 0;
                    list.add(redisCommand);
                } else {

                    if (!line.startsWith("#") && line.split("=").length == 2) {
                        String key = line.split("=")[0];
                        String value = line.split("=")[1];
                        if (line.startsWith("loginName")) {
                            redisCommand.setLoginName(line.split("=")[1]);
                        } else if (line.startsWith("systemType")) {
                            redisCommand.setSystemType(line.split("=")[1]);
                        } else if (line.startsWith("times")) {
                            redisCommand.setTimes(Integer.valueOf(line.split("=")[1]));
                        } else if (line.startsWith("sourceType")) {
                            redisCommand.setSourceType(line.split("=")[1]);
                        } else if (line.startsWith("memberNo")) {
                            redisCommand.setMemberNo(line.split("=")[1]);
                        } else if (line.startsWith("tokenId")) {
                            redisCommand.setTokenId(line.split("=")[1]);
                        } else if (key.equalsIgnoreCase("key")) {
                            redisCommand.setKey(value);
                        } else if (key.equalsIgnoreCase("value")) {
                            redisCommand.setValue(value);
                        } else if (key.equalsIgnoreCase("time")) {
                            redisCommand.setTime(Integer.parseInt(value));
                        }
                    }

                }
            }
        }
        return list;
    }

    //给所有文件去掉#
    public static void dealFile(String path) {
        File folder = new File(path);
        if (folder.getName().contains("#")) {
            File file = new File(folder.getPath().replace("#", ""));
            folder.renameTo(file);
        }
        if (folder.isDirectory()) {
            File[] interfaces = folder.listFiles();
            for (File interfaceFolder : interfaces) {
                dealFile(interfaceFolder.getPath());
            }
        }
    }

    //删除beforeClass和afterClass的文件内容
    public static void dealDBFile(String path) {
        File folder = new File(path);
        if (folder.getName().equals("beforeClass") || folder.getName().equals("afterClass")) {
            String paths = folder.getPath();
            FileUtil.fileWrite(paths, "");
        }
        if (folder.isDirectory()) {
            File[] interfaces = folder.listFiles();
            for (File interfaceFolder : interfaces) {
                dealDBFile(interfaceFolder.getPath());
            }
        }
    }

    public static void main(String[] args) {
//        ReadSuite re = new ReadSuite();
//        re.readSuitFile();
        dealFile("suite");
//        dealDBFile("suite");
    }

}
