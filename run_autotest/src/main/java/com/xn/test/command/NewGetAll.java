package com.xn.test.command;

import com.xn.test.common.NewReflect;
import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLClassLoader;

import static com.xn.test.util.ReflectionUtils.addJar;
import static com.xn.test.util.ReflectionUtils.loadClass;

/**
 * Created by Administrator on 2016/8/22.
 */

public class NewGetAll {
    private final static Logger logger = LoggerFactory.getLogger(NewGetAll.class);
    public static StringBuffer result = new StringBuffer();
    public static URLClassLoader loader=addJar("C:\\Users\\xn056839\\Downloads\\user-interface-2.0.0-20160926.085005-12.jar");

    public static void getParam(String interfaceName, URLClassLoader loader,String writePath) throws Exception {
        result.setLength(0);
        String interface_Name = interfaceName;

        Class<?> c = null;
        try {
            c = loadClass(interfaceName,loader);

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                result.setLength(0);
                String methodName = method.getName();
                //获得调用的接口的所有方法
                result.append("interfaceName=" + interface_Name + "\n");
                result.append("methodName=" + methodName + "\n");

                String folder=writePath+"suite/"+interface_Name+"/"+methodName+"/";
                Type[] types = method.getGenericParameterTypes();//参数类型
                Type returnType = method.getGenericReturnType();// 返回类型
                String[] returnTypeList = returnType.toString().split("<");
                for (int i = 0; i < returnTypeList.length; i++) {
                    returnTypeList[i] = returnTypeList[i].substring(returnTypeList[i].lastIndexOf(".") + 1);
                }
//                result.append("returnType=" + Joiner.on("<").join(returnTypeList) + "\n");
                for (Type type : types) {
                    String parname = type.toString().substring(6);
                    String parname_short = parname.substring(parname.lastIndexOf(".") + 1);
//                    result.append("paramType=" + parname_short + "\n");
                    c = loadClass(parname,loader);
                    if (!NewReflect.isBaseDataType(c)) {
                        NewReflect f = new NewReflect(c);
                        f.getSetClass(c,  folder,StringUtil.firstToLow(parname_short));
                    }
                }
                FileUtil.fileWrite(folder+"config.properties",result.toString());
                FileUtil.fileWrite(folder+"beforeClass","");
                FileUtil.fileWrite(folder+"afterClass","");
                StringBuffer config=new StringBuffer();
                config.append("url=\n");
                config.append("appName=\n");
                config.append("timeout=\n");
                config.append("version=\n");
                config.append("group=\n");
                FileUtil.fileWrite(writePath+"suite/"+interface_Name+"/serviceConfig.properties",config.toString());
            }

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

//            getParam("cn.xn.user.service.ILoginService");
//            getParam("cn.xn.user.service.ICustomerInfoService");
//            getParam("cn.xn.user.service.IFriendService");
//            getParam("cn.xn.user.service.ICustomerInfoService");
//            getParam("cn.xn.user.service.IPwdService");
//            getParam("cn.xn.user.service.IRegisterService");
//            getParam("com.xiaoniu.dataplatform.tongduncredit.service.ITongDunCreditService");
            getParam("cn.xn.user.service.ICustomerInfoService",loader,"e:\\");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
