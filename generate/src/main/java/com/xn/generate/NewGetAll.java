package com.xn.generate;

import com.xn.test.common.NewReflect;
import com.xn.test.mail.JavaMailWithAttachment;
import com.xn.test.util.FileUtil;

import com.xn.test.util.ReflectionUtils;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLClassLoader;




/**
 * Created by Administrator on 2016/8/22.
 */

public class NewGetAll {
    private final static Logger logger = LoggerFactory.getLogger(NewGetAll.class);
    public static StringBuffer result = new StringBuffer();
    public static URLClassLoader loader = null;

    public static void getParam(String interfaceName, URLClassLoader loader, String writePath) throws Exception {
        result.setLength(0);
        String interface_Name = interfaceName;

        Class<?> c = null;
        try {
            c = ReflectionUtils.loadClass(interfaceName, loader);

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                result.setLength(0);
                String methodName = method.getName();
                //获得调用的接口的所有方法
                result.append("interfaceName=" + interface_Name + "\n");
                result.append("methodName=" + methodName + "\n");

                String folder = writePath + "/suite/" + interface_Name + "/" + methodName + "/";
                Type[] types = method.getGenericParameterTypes();//参数类型
                Type returnType = method.getGenericReturnType();// 返回类型
                String[] returnTypeList = returnType.toString().split("<");
                for (int i = 0; i < returnTypeList.length; i++) {
                    returnTypeList[i] = returnTypeList[i].substring(returnTypeList[i].lastIndexOf(".") + 1);
                }
                for (Type type : types) {
                    String parname = type.toString().substring(6);
                    String parname_short = parname.substring(parname.lastIndexOf(".") + 1);

                    c = ReflectionUtils.loadClass(parname, loader);
                    if (!NewReflect.isBaseDataType(c)) {
                        NewReflect f = new NewReflect(c);
                        f.getSetClass(c, folder, StringUtil.firstToLow(parname_short));
                    }
                }
                FileUtil.fileWrite(folder + "config.properties", result.toString());
                FileUtil.fileWrite(folder + "beforeClass.txt", "");
                FileUtil.fileWrite(folder + "afterClass.txt", "");
                StringBuffer config = new StringBuffer();
                config.append("url=\n");
                config.append("appName=\n");
                config.append("timeout=\n");
                config.append("version=\n");
                config.append("group=\n");
                FileUtil.fileWrite(writePath + "suite/" + interface_Name + "/serviceConfig.properties", config.toString());

            }
            //数据库配置文件
            String jdbcString = "jdbc_url=\njdbc_username=\njdbc_password=";
            FileUtil.fileWrite(writePath + "suite/jdbc.properties", jdbcString);
            String redisString="redis.slaver.host1=\n" +
                    "redis.slaver.port1=\n" +
                    "redis.slaver.host2=\n" +
                    "redis.slaver.port2=\n" +
                    "redis.slaver.host3=\n" +
                    "redis.slaver.port3=\n" +
                    "redis.timeout=\n" +
                    "redis.max.redirections=";
            FileUtil.fileWrite(writePath + "suite/redis.properties", redisString);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("interface or class is not exit");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("write file Exception");
        }
//        System.out.println(result);

        return;
    }

    public static void main(String[] args) {

        try {
            if (args.length != 3) {
                logger.error("输入参数错误：[依赖jar地址] [要测试服务名] [测试文件接收邮箱地址]");
                return ;
            }
            loader = ReflectionUtils.addJar(args[0]);

            getParam(args[1], loader, "/data/autotest/generate/");

            logger.warn("存放地址在 {}suite","/data/autotest/generate/" );
            String zipOut="/data/autotest/generate/suite.zip";
            FileZip fileZip=new FileZip();
            fileZip.zipFile("/data/autotest/generate/suite",zipOut);
            JavaMailWithAttachment se = new JavaMailWithAttachment(true);
            File affix = new File(zipOut);
            se.doSendHtmlEmail("dubbo接口测试文件", "dubbo接口测试文件", args[2], affix);
            affix.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
