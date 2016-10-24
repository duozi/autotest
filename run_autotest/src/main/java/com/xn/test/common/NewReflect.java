package com.xn.test.common;


import com.xn.test.util.FileUtil;
import com.xn.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxi.zhou on 2016/2/28.
 */

public class NewReflect {
    /**
     * @desc 通过反射来动态调用get 和 set 方法
     * @date 2010-10-14
     * @Version 1.0
     */
    private static Logger logger = LoggerFactory.getLogger(NewReflect.class);
    private Class cls;

    /**
     * 存放set方法
     */
    public List<String> setMethods = new ArrayList<>();
    public ArrayList<Method> getMethods = new ArrayList<>();

    /**
     * 定义构造方法 -- 一般来说是个pojo
     *
     * @param c 目标对象
     */
    public NewReflect(Class c) {
        cls = c;
        initMethods();
    }

    /**
     * @desc 初始化
     */
    public void initMethods() {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                getMethods.add(method);
            }

        }
    }

    /**
     * @desc 调用get方法
     */
    public String getMethodValue(String property) {

        for (Method m : getMethods) {
            if (m.getName().toLowerCase().contains(property)) {
                try {
                    /**
                     * 调用obj类的getter函数
                     */

                    logger.debug("{} default value is {}", property, String.valueOf(m.invoke(cls.newInstance())));
                    String defaultValue = String.valueOf(m.invoke(cls.newInstance()));
                    if (!defaultValue.equals("null")) {
                        return defaultValue;
                    }
                    return "";
                } catch (Exception ex) {
                    System.out.println("invoke getter on " + property + " error: "
                            + ex.toString());
                    return "";
                }
            }
        }
        return "";
    }

    public void getSetClass(Class cls, String folder, String para) {
        StringBuffer result = new StringBuffer();
        setMethods = new ArrayList<>();

        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && !methodName.equals("setSign")) {
                String pro = methodName.substring(3).toLowerCase();
                String defaultValue = getMethodValue(pro);

                setMethods.add(StringUtil.firstToLow(methodName.substring(3)) + "=" + defaultValue );
            }

        }

         Collections.sort(this.setMethods);
        for (String s : this.setMethods) {

            result.append( s + "\n");
        }

        FileUtil.fileWrite(folder + "demo_1/demo_1", result.toString());
        FileUtil.fileWrite(folder + "demo_1/assert", "");
        FileUtil.fileWrite(folder+"demo_1/before","");
        FileUtil.fileWrite(folder+"demo_1/after","");
    }


    /**
     * 判断一个类是否为基本数据类型。
     *
     * @param clazz 要判断的类。
     * @return true 表示为基本数据类型。
     */
    public static boolean isBaseDataType(Class clazz) throws Exception {
        return
                (
                        clazz.equals(String.class) ||
                                clazz.equals(Integer.class) ||
                                clazz.equals(Byte.class) ||
                                clazz.equals(Long.class) ||
                                clazz.equals(Double.class) ||
                                clazz.equals(Float.class) ||
                                clazz.equals(Character.class) ||
                                clazz.equals(Short.class) ||
                                clazz.equals(BigDecimal.class) ||
                                clazz.equals(BigInteger.class) ||
                                clazz.equals(Boolean.class) ||
                                clazz.equals(Date.class) ||
                                clazz.isPrimitive() ||
                                clazz.getName().contains("java")
                );
    }


}

