package com.xn.test.util;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.xn.test.Exception.CaseErrorEqualException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxi.zhou on 2016/3/9.
 */
public class StringUtil {
    private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);
    private static String SYSTEM_TYPE = "systemType";

    public static String firstToLow(String s) {

        String str;
        if (s.length() > 2) {
            str = s.substring(0, 1).toLowerCase() + s.substring(1);
        } else {
            str = s;
        }
        return str;
    }

    public static String firstToUp(String s) {

        String str;
        if (s.length() > 2) {
            str = s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            str = s;
        }
        return str;
    }

    public static String getPro(String file, String properName) {
        Properties prop = new Properties();
        String value = null;
        InputStream in = Object.class.getResourceAsStream("/" + file);
        try {
            prop.load(in);
            value = prop.getProperty(properName).trim();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("properties file is not exist");
        } finally {
            return value;
        }
    }

    public static Boolean isEmpty(Object value) {
        if (value == null) return true;
        if (org.apache.commons.lang.StringUtils.isBlank(value.toString())) return true;
        return false;
    }

    public static boolean isJson(Object value) {
        if (!(value instanceof String)) return false;
        String json = value.toString();
        return json.startsWith("{") && json.endsWith("}");
    }

    public static String getConfig(File file, String properName, String defaultValue) {

        List<String> lines = FileUtil.fileReadeForList(file);
        for (String line : lines) {
            if (line.split("=").length == 2) {
                if (line.split("=")[0].equals(properName)) {
                    return line.split("=")[1];

                }

            }
        }
        return defaultValue;
    }

    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    public static String lastName(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public static List<String> listAddSign(List<String> lines) throws CaseErrorEqualException {
        String param = "";
        String key = "";
        for (String line : lines) {
            param += line + "&";
            if (line.startsWith("systemType")) {
                if(line.split("=").length!=2){
                    throw new CaseErrorEqualException("systemType is not exist,cannot add sign");
                }
                String value = line.split("=")[1];
                key = getPro("test.properties", "key." + value);
            }
        }
        param += "key=" + key;
        String sign = string2MD5(param);

        lines.add("sign=" + sign);
        return lines;

    }

    public static String getSign(String para) {
        Pattern p1 = Pattern.compile("set(.*?)\\(");
        Pattern p2 = Pattern.compile("\\((.*?)\\)");
        Matcher m;
        String type;
        String value;
        String key = "";
        StringBuffer result = new StringBuffer();
        String[] list = para.split("\\n");
        for (String line : list) {
            if (!line.contains("%")) {
                m = p1.matcher(line);
                if (m.find()) {
                    type = firstToLow(m.group(1));

                    m = p2.matcher(line);
                    if (m.find()) {
                        if (m.group(1).contains("\"")) {
                            value = firstToLow(m.group(1)).replace("\"", "");
                        } else {
                            value = firstToLow(m.group(1));
                        }
                        //值为“”代表参数没有填，计算sign的时候就不要加上这一项,值为“ ”代表填入的是空的，计算sign的时候还是要加上这一项的

                        if (type.equals("systemType")) {
                            if (!StringUtils.isNotEmpty(value)) {
                                return "%";
                            } else {
                                key = getPro("test.properties", "key." + value);
                            }
                        }
                        if (!value.equals("")) {
                            if (value.equals(" ")) {
                                value = "";
                            }
                            result.append(type + "=" + value + "&");
                        }

                    }
                }

            }
        }

        result.append("key=" + key);
        String sign = string2MD5(result.toString());

        return sign;
    }


    //生成随机手机号
    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String addLoginName() {
        String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;

    }

    public static void main(String[] args) {
        System.out.println(addLoginName());

//        String s = getSign("checkLoginReq.setAppVersion(\"2.4.0\");\n" +
//                "\t\tcheckLoginReq.setMemberNo(\"05135e9b-7fdb-478a-ba40-569bdf8b7331\");\n" +
//                "\t\tcheckLoginReq.setSign(\"%\");\n" +
//                "\t\tcheckLoginReq.setSourceType(\"android\");\n" +
//                "\t\tcheckLoginReq.setSystemType(\"QGZ\");\n" +
//                "\t\tcheckLoginReq.setTokenId(\"ef8bd754-5124-47c1-b763-57b29010ea45\");");
//
//        System.out.println(s);

//        String str = (String) JOptionPane.showInputDialog(null, "参数：\n", "获得sign", JOptionPane.PLAIN_MESSAGE, null, null,
//                null);
//        JOptionPane.showMessageDialog(null, getSign(str), "显示ID",JOptionPane.PLAIN_MESSAGE);
//

    }


}
