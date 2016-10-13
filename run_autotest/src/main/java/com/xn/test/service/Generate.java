package com.xn.test.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;
import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Generate {
    private static final Logger logger = LoggerFactory.getLogger(Generate.class);
    public static String caseStr = "    @Test\n" +
            "    public void test_${demoName}() {\n" +
            "        ${paramType} ${paramTypeImpl} = new ${paramType}();\n" +
            "${fileInPara}\n" +
            "        ${paramTypeImpl}.setSign(StringUtil.addSign(${paramTypeImpl}));\n" +
            "\n" +
            "        ${returnType} result = ${interfaceNameImpl}.${methodName}(${paramTypeImpl});\n" +
            "        logger.warn(result.toString());\n" +
            "${assert}" +
            "    }\n";
    String interfaceName;
    String methodName;
    String returnType;
    String paramType;
    String beforeClass;
    String afterClass;
    String before;
    String after;
    String allTest;

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public void readSuitFile() {
        File folder = new File("suit");
        File[] interfaces = folder.listFiles();
        //接口名层
        for (File interfaceFolder : interfaces) {
            if (interfaceFolder.isDirectory()) {
                File[] methods = interfaceFolder.listFiles();
                //方法名层
                for (File methodFolder : methods) {
                    File[] files = methodFolder.listFiles();
                    //配置文件和case
                    interfaceName = "";
                    methodName = "";
                    returnType = "";
                    paramType = "";
                    beforeClass = "";
                    afterClass = "";
                    before = "";
                    after = "";
                    allTest = "";
                    for (File file : files) {

                        if (file.getName().equals("config.properties")) {
                            interfaceName = StringUtil.getConfig(file, "interfaceName","");
                            methodName = StringUtil.getConfig(file, "methodName","");
                            returnType = StringUtil.getConfig(file, "returnType","");
                            paramType = StringUtil.getConfig(file, "paramType","");
                        } else if (file.getName().equals("beforeClass")) {
                            beforeClass = dealBeforeClassFile(file);
                        } else if (file.getName().equals("afterClass")) {
                            afterClass = dealAfterClassFile(file);
                        } else if (file.getName().equals("before")) {
                            before = dealFile(file);
                        } else if (file.getName().equals("after")) {
                            after = dealFile(file);
                        } else if (file.isDirectory()) {
                            File[] fs = file.listFiles();
                            String testStr = "";
                            String assertStr = "";
                            String par = "";
                            for (File f : fs) {

                                if (f.getName().startsWith("demo")) {
                                    //一个test里面的参数
                                    par = FileUtil.fileReadeForStr(f);

                                } else if (f.getName().equals("assert")) {
                                    assertStr = dealAssertFile(f);

                                }

                            }
                            //case文件名作为test的名字
                            testStr = caseStr.replace("${demoName}", file.getName()).replace("${fileInPara}", par).replace("${assert}", assertStr);
                            allTest += testStr;
                        }


                    }
                    generateTestFile();
                }

            }
        }
    }

    public static String dealAssertFile(File file) {
        String result = "";
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            String type = line.split("=")[0];
            String value = line.split("=")[1];
            result += "\t\tassertEquals(result.get" + StringUtil.firstToUp(type) + "().toString(), \"" + value + "\");\n";
        }
        return result;
    }

    public static String dealBeforeClassFile(File file) {
        String result = "";
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.equalsIgnoreCase("DB")) {
                result += "\t\tDBService.newDB();\n";
            } else if (line.startsWith("DB")) {
                if (!result.contains("DBService.newDB()")) {
                    result += "\t\tDBService.newDB();\n";
                }
                String sql = line.split("=", 2)[1];
                if (sql.toLowerCase().startsWith("select")) {
                    result += "\t\tDBService.selectFromDB(\"" + sql + "\");\n";
                } else {
                    result += "\t\tDBService.updateDB(\"" + sql + "\");\n";
                }
            }
        }
        return result;
    }

    public static String dealAfterClassFile(File file) {
        String result = "";
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.equalsIgnoreCase("DB")) {
                result += "\t\tDBService.closeDB();\n";
            } else if (line.startsWith("DB")) {
                if (!result.contains("DBService.newDB()")) {
                    result += "\t\tDBService.newDB();\n";
                }
                String sql = line.split("=", 2)[1];
                if (sql.toLowerCase().startsWith("select")) {
                    result += "\t\tDBService.selectFromDB(\"" + sql + "\");\n";
                } else {
                    result += "\t\tDBService.updateDB(\"" + sql + "\");\n";
                }
            }
        }
        return result;
    }

    public static String dealFile(File file) {
        String result = "";
        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.startsWith("DB")) {
                String sql = line.split("=", 2)[1];
                if (sql.toLowerCase().startsWith("select")) {
                    result += "\t\tDBService.selectFromDB(\"" + sql + "\");\n";
                } else {
                    result += "\t\tDBService.updateDB(\"" + sql + "\");\n";
                }
            }
        }
        return result;
    }

    public void generateTestFile() {
        InputStream in = Object.class.getResourceAsStream("/template.txt");

        try {
            String content = IOUtils.toString(in, "UTF-8");


            String writeStr = content.replace("${case}", allTest).replace("${methodName}", methodName).replace("${interfaceName}", interfaceName).replace("${interfaceNameImpl}", StringUtil.firstToLow(interfaceName)).replace("${paramType}", paramType).replace("${paramTypeImpl}", StringUtil.firstToLow(paramType)).replace("${returnType}", returnType).replace("${beforeClass}", beforeClass).replace("${afterClass}", afterClass).replace("${before}", before).replace("${after}", after);
            FileUtil.fileWrite("case/" + interfaceName + "/" + methodName + "Test.java", writeStr);


        } catch (IOException e) {
            e.printStackTrace();
            logger.error("file is not exist");
        }
        return;
    }

    public static void main(String[] args) {
        Generate g = new Generate();
        g.readSuitFile();
    }
}
