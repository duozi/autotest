package com.xn.generate;


import com.xn.common.mail.JavaMailWithAttachment;
import com.xn.common.service.GetPara;
import com.xn.common.util.FileUtil;
import com.xn.common.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URLClassLoader;

import static com.xn.common.util.JarUtil.addJar;


/**
 * Created by Administrator on 2016/8/22.
 */

public class GenerateDubboSuite {
    private final static Logger logger = LoggerFactory.getLogger(GenerateDubboSuite.class);
    public static StringBuffer result = new StringBuffer();
    public static URLClassLoader loader = null;

    public void getDubboSuite(String interfaceName, String writePath) {
        result.setLength(0);
        String interface_Name = interfaceName;

        Class<?> c = null;
        try {
            c = ReflectionUtils.loadClass(interfaceName);

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                result.setLength(0);
                String methodName = method.getName();
                //获得调用的接口的所有方法
                result.append("interfaceName=" + interface_Name + "\r\n");
                result.append("methodName=" + methodName + "\r\n");
                result.append("useSign=true\r\n");
                result.append("signType=md5\r\n");

                String folder = writePath + "suite/dubbo/" + interface_Name + "/" + methodName + "/";
                GetJsonServiceImpl getJsonService = new GetJsonServiceImpl();
                String paramResult = getJsonService.getJson(interfaceName, methodName);
                FileUtil.fileWrite(folder + "demo_1/demo_1", paramResult.toString());
                FileUtil.fileWrite(folder + "demo_1/assert", "");
                FileUtil.fileWrite(folder + "demo_1/before", "");
                FileUtil.fileWrite(folder + "demo_1/after", "");
                FileUtil.fileWrite(folder + "config.properties", result.toString());
                FileUtil.fileWrite(folder + "beforeClass", "");
                FileUtil.fileWrite(folder + "afterClass", "");
                StringBuffer config = new StringBuffer();
                config.append("#是否通过zk调用，如果是true,那属性zk必填，如果是false,那将使用点对点直连，url属性必填\r\nuseZk=\r\n");
                config.append("url=\r\n");
                config.append("zk=\r\n");
                config.append("appName=\r\n");
                config.append("timeout=\r\n");
                config.append("version=\r\n");
                config.append("group=\r\n");

                FileUtil.fileWrite(writePath + "suite/dubbo/" + interface_Name + "/serviceConfig.properties", config.toString());

            }
            //数据库配置文件
            String jdbcString = "db_name=\r\n.jdbc_url=\r\n.jdbc_username=\r\n.jdbc_password=";
            FileUtil.fileWrite(writePath + "suite/jdbc.properties", jdbcString);
            //redis 配置文件
            String redisString = "redis.slaver.host1=\r\n" +
                    "redis.slaver.port1=\r\n" +
                    "redis.slaver.host2=\r\n" +
                    "redis.slaver.port2=\r\n" +
                    "redis.slaver.host3=\r\n" +
                    "redis.slaver.port3=\r\n" +
                    "redis.timeout=\r\n" +
                    "redis.max.redirections=";
            FileUtil.fileWrite(writePath + "suite/redis.properties", redisString);

            String keyString="key.QGZ=";
            FileUtil.fileWrite(writePath+"suite/key.properties",keyString);
            String jarString="repository=\r\nartifact=\r\ngroup=\r\nversion=";
            FileUtil.fileWrite(writePath+"suite/jar.properties",jarString);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(result);

        return;
    }

    public static void main(String[] args) {

        try {
            if (args.length != 3) {
                logger.error("输入参数错误：[依赖jar地址] [要测试服务名] [测试文件接收邮箱地址]");
                return;
            }

            loader = addJar(args[0]);
            GetPara getPara = new GetPara();
            getPara.setLoader(loader);
            GenerateDubboSuite generateDubboSuite = new GenerateDubboSuite();
            String[] service = args[1].trim().split(",");
//            String[] service = "cn.xn.user.service.ICustomerInfoService".trim().split(",");
            for (String s : service) {
                generateDubboSuite.getDubboSuite(s, "/data/autotest/user/generate/");
//                getParam(s, loader, "d:\\test\\");
            }

            logger.info("存放地址在 {}suite", "/data/autotest/user/generate/");
            String zipOut = "/data/autotest/user/generate/suite.zip";
            FileZip fileZip = new FileZip();
            fileZip.zipFile("/data/autotest/user/generate/suite", zipOut);


            JavaMailWithAttachment se = new JavaMailWithAttachment(true);

            se.doSendHtmlEmail("dubbo接口测试文件结构", "这是程序自动生成的dubbo接口测试文件目录结构，文件名为suite不能修改,其余case可以参照现有的结构编写", args[2], zipOut);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
