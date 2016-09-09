package com.xn.test.service;

import com.google.common.base.Joiner;
import com.xn.test.common.NewReflect;
import com.xn.test.common.Reflect;
import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/8/22.
 */

public class NewGetAll {
    private final static Logger logger = LoggerFactory.getLogger(NewGetAll.class);
    public static StringBuffer result = new StringBuffer();


    public static void getParam(String interfaceName) throws Exception {
        result.setLength(0);
        String interface_Name = interfaceName;

        Class<?> c = null;
        try {
            c = Class.forName(interfaceName);

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                result.setLength(0);
                String methodName = method.getName();
                //获得调用的接口的所有方法
                result.append("interfaceName=" + interface_Name + "\n");
                result.append("methodName=" + methodName + "\n");
                result.append("url=\n");
                result.append("appName=\n");
                result.append("timeout=\n");
                result.append("version=\n");
                result.append("group=\n");
                String folder="suite/"+interface_Name+"/"+methodName+"/";
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
                    c = Class.forName(parname);
                    if (!NewReflect.isBaseDataType(c)) {
                        NewReflect f = new NewReflect(c);
                        f.getSetClass(c,  folder,StringUtil.firstToLow(parname_short));
                    }
                }
                FileUtil.fileWrite(folder+"config.properties",result.toString());
                FileUtil.fileWrite(folder+"beforeClass","");
                FileUtil.fileWrite(folder+"afterClass","");
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
            getParam("cn.xn.user.service.ILoginService");
//            getParam("cn.xn.user.service.ICustomerInfoService");
//            getParam("cn.xn.user.service.IFriendService");
//            getParam("cn.xn.user.service.ICustomerInfoService");
//            getParam("cn.xn.user.service.IPwdService");
//            getParam("cn.xn.user.service.IRegisterService");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
