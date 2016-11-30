package com.xn.common.util;


import com.xn.common.service.DBService;
import com.xn.common.service.GetPara;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by xn056839 on 2016/8/30.
 */

public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static Connection con;
    private static BasicDataSource bds = null;
    public static HashMap<String, DBService> DbMap = new HashMap<String, DBService>();

    public static boolean DBInit() {
        GetPara getPara = new GetPara();
        String path = getPara.getPath();
        File file = new File(path + "suite/jdbc.properties");
        String names = StringUtil.getConfig(file, "db_name", "");
        String[] dbName = names.split(",");
        for (String name : dbName) {
            String url = StringUtil.getConfig(file, name + ".jdbc_url", "");
            String user = StringUtil.getConfig(file, name + ".jdbc_username", "");
            String pwd = StringUtil.getConfig(file, name + ".jdbc_password", "");
            DBService dbService = new DBService(url, user, pwd);

            try {
                dbService.newDB();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            DbMap.put(name, dbService);
            logger.info("new db ----{}", name);
        }
        return true;
    }

    public static void DBClose() {
        for (String name : DbMap.keySet()) {
            DBService dbService = DbMap.get(name);
            dbService.closeDB();
        }
    }

    public static String getCountFromDB(String name, String sql) {
        DBService dbService = DbMap.get(name);
        return dbService.getCountFromDB(sql);
    }
    public static ResultSet selectFromDB(String name,String sql) {
        DBService dbService = DbMap.get(name);
        return dbService.selectFromDB(sql);
    }
    public static int updateDB(String name, String sql) {
        DBService dbService = DbMap.get(name);
        return dbService.updateDB(sql);
    }
    public static void main(String[] args) {
//        selectFromDB("");
//        int i=updateDB("UPDATE customer_info set ENC_TYPE=\"KK\" WHERE MOBILE=\"18514762028\"");
//        System.out.println(i);
        String s = "12";
        String[] s1 = s.split(",");
        System.out.println(s1[0]);
    }
}
