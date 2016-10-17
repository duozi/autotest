package com.xn.generate;

import com.xn.test.common.NewReflect;
import com.xn.test.util.FileUtil;
import com.xn.test.util.ReflectionUtils;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

                String folder = writePath + "suite/" + interface_Name + "/" + methodName + "/";
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
                FileUtil.fileWrite(folder + "beforeClass", "");
                FileUtil.fileWrite(folder + "afterClass", "");
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
        String path = "e:\\";
        try {
//            if (args.length != 2) {
//                logger.error("输入参数错误：[依赖jar地址] [要测试服务名]");
//                return ;
//            }
//            loader = ReflectionUtils.addJar(args[0]);
            loader=ReflectionUtils.addJar("D:\\ruleengine-skeleton-1.0.0.jar");
//            getParam(args[1], loader, "e:\\");
            getParam("com.xiaoniu.dataplatform.ruleengine.service.IRuleEngineService",loader,"e:\\");
            logger.warn("存放地址在本地 {}suite", path);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
