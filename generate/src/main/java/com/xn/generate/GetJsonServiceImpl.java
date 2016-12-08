package com.xn.generate;/**
 * Created by xn056839 on 2016/11/10.
 */


import com.xn.common.util.ReflectionUtils;
import com.xn.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class GetJsonServiceImpl {
    private final static Logger logger = LoggerFactory.getLogger(GetJsonServiceImpl.class);
    public static StringBuffer result = new StringBuffer();
    public static StringBuffer before = new StringBuffer();
    public static int i = 1;


    public String getJson( String interfaceName, String methodName) throws Exception {
        result.setLength(0);
        before.setLength(0);
        i = 1;
        Class<?> c = null;
        try {
            c = ReflectionUtils.loadClass(interfaceName);
            StringUtil.addParamStartString(result,before);
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                String name = method.getName();
                //获得调用的接口的方法
                if (name.endsWith(methodName)) {
                    Type[] types = method.getGenericParameterTypes();

                    for (Type type : types) {
//                        StringUtil.addXMLStartString(result, "var" + i, before);
//                        result.append("\n");
                        String parname = StringUtils.EMPTY;
                        if (type.toString().startsWith("java.util.List")) {
                            parname = type.toString().substring(15, type.toString().length() - 1);
                            c = ReflectionUtils.loadClass(parname);
                            if (!JsonReflect.isBaseDataType(c)) {
                                StringUtil.addJsonStartList(result, before);
                                StringUtil.addHead(result, before);
                                JsonReflect f = new JsonReflect(c);
                                f.getSuperClass(c, result, before);
                                StringUtil.addEnd(result, before);
                                StringUtil.addJsonEndList(result, before);
                            } else if (parname.equals("java.lang.String")) {
                                result.append("[\"\"],\n");
                            } else {
                                result.append("[],\n");
                            }

                        } else {
                            parname = type.toString().substring(6);


                            c =ReflectionUtils.loadClass(parname);
                            if (!JsonReflect.isBaseDataType(c)) {
                                StringUtil.addHead(result, before);
                                JsonReflect f = new JsonReflect(c);
                                f.getSuperClass(c, result, before);
                                StringUtil.addEnd(result, before);

                            }
                        }

                    }
                    break;
                }
            }
            StringUtil.addParamEndString(result,before);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("interface or class is not exit");
        }
//        logger.info("{}", result);
//        System.out.println(result);
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
//        URLClassLoader loader = JarUtil.addJar("d:/jar/");
        GetJsonServiceImpl getJsonService = new GetJsonServiceImpl();
        getJsonService.getJson( "cn.xn.user.controller.ICustomerInfoService", "updateRefereeInfo");
//        String s = "{\"param\":[{\"appVersion\":\"\",\"sourceType\":\"\",\"systemType\":\"\",\"sign\":\"\",\"memberNo\":\"\",\"refereeNo\":\"\",\"refereeId\":\"\",\"days\":3,\"hour\":72,\"isDay\":true},{\"interaction\":false}]}";
//        JSONObject o= JSONObject.fromObject(s);
//        JSONArray array=o.getJSONArray("param");
//        System.out.println(array.getString(1));
//        System.out.println(array.getString(0));
//
//        Class<?> c = null;
//
//        c = loader.loadClass("cn.xn.user.controller.ICustomerInfoService");
//
//        Method[] methods = c.getDeclaredMethods();
//        Type[] types=null;
//        for (Method method : methods) {
//            String name = method.getName();
//            //获得调用的接口的方法
//            if (name.endsWith("updateRefereeInfo")) {
//               types = method.getGenericParameterTypes();
//
//            }
//        }
//        KeyValueStore keyValueStore=new KeyValueStore("var1",s);
//        List list=new ArrayList();
//        list.add(keyValueStore);
//       Object[] object= BeanUtils.getParameters(list,types);
    }



}