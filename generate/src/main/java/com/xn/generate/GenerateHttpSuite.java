package com.xn.generate;/**
 * Created by xn056839 on 2016/10/31.
 */

import com.xn.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateHttpSuite {
    private static final Logger logger = LoggerFactory.getLogger(GenerateHttpSuite.class);
    public static StringBuffer result = new StringBuffer();

    public  void getHttpSuite(String writePath) {
        String path = writePath + "suite/http/";
        result.setLength(0);

        String configString = "url=\r\ntimeout=\r\nuseSign=\r\nsignType=\r\nrequestType=\r\nparamType=";
        FileUtil.fileWrite(path + "/interfaceName/config.properties", configString);
        FileUtil.fileWrite(path + "/interfaceName/beforeClass", "");
        FileUtil.fileWrite(path + "/interfaceName/afterClass", "");


        FileUtil.fileWrite(path + "/interfaceName/caseName/before", "");
        FileUtil.fileWrite(path + "/interfaceName/caseName/after", "");
        FileUtil.fileWrite(path + "/interfaceName/caseName/assert", "");
        FileUtil.fileWrite(path + "/interfaceName/caseName/caseName", "");

        //数据库配置文件
        String jdbcString = "db_name=\r\n.jdbc_url=\r\n.jdbc_username=\r\n.jdbc_password=";
        FileUtil.fileWrite(writePath + "suite/jdbc.properties", jdbcString);
        //redis配置文件
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
//        System.out.println(result);

        return;
    }

    public static void main(String[] args) {

//        getParam("d:/httptest");
    }
}
