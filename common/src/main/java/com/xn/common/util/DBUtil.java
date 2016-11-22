package com.xn.common.util;


import com.xn.common.service.GetPara;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;

/**
 * Created by xn056839 on 2016/8/30.
 */

public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;
    public static String path;
    private static BasicDataSource bds = null;


    public static boolean newDB() {
        GetPara getPara = new GetPara();
        path = getPara.getPath();
        File file = new File(path + "suite/jdbc.properties");
        String url = StringUtil.getConfig(file, "jdbc_url", "");
        String user = StringUtil.getConfig(file, "jdbc_username", "");
        String pwd = StringUtil.getConfig(file, "jdbc_password", "");

        bds = new BasicDataSource();
        //设置驱动程序
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        //设置连接用户名
        bds.setUsername(user);
        //设置连接密码
        bds.setPassword(pwd);
        //设置连接地址
        bds.setUrl(url);
        //设置初始化连接总数
        bds.setInitialSize(50);
        //设置同时应用的连接总数
        bds.setMaxActive(5);
        //设置在缓冲池的最大连接数
        bds.setMaxIdle(2);
        //设置在缓冲池的最小连接数
        bds.setMinIdle(0);
        //设置最长的等待时间
        bds.setMaxWait(5);
        try {
            con = bds.getConnection();


            logger.info("new DB connection");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//
////            File file = new File(path + "suite/jdbc.properties");
////            String url = StringUtil.getConfig(file, "jdbc_url", "");
////            String user = StringUtil.getConfig(file, "jdbc_username", "");
////            String pwd = StringUtil.getConfig(file, "jdbc_password", "");
//
//
//
//
//            if (!url.equals("") && !user.equals("") && !pwd.equals("")) {
//                con = (Connection) DriverManager.getConnection(url, user, pwd);
//
//                stmt = con.createStatement();
//                logger.info("new DB connection");
//                return true;
//            }
//        }  catch (SQLException e) {
//            e.printStackTrace();
//        }
        return false;
    }

    public static void closeDB() {
        try {
//            rs.close();
            //关闭语句
//            stmt.close();
            //关闭连接
            bds.close();
            logger.info("close DB connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ResultSet selectFromDB(String sql) {
        try {
            Statement stmt = con.createStatement();
            logger.info("execute sql:{}", sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {
//                    System.out.println(i + 1);
//                    System.out.println(rs.getString(i + 1));
                }
//                System.out.println();
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return rs;
        }


    }

    public static String getCountFromDB(String sql) {
        String count = "-1";
        try {
            Statement stmt = con.createStatement();
            logger.info("execute sql:{}", sql);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                count = rs.getString(1);
                logger.info(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return count;
        }
    }

    public static int updateDB(String sql) {
        int rs = -1;
        try {
            Statement stmt = con.createStatement();
            logger.info("execute sql:{}", sql);
            rs = stmt.executeUpdate(sql);
//            System.out.println(rs.getString(1));
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return rs;
        }


    }

    public static void main(String[] args) {
//        selectFromDB("");
//        int i=updateDB("UPDATE customer_info set ENC_TYPE=\"KK\" WHERE MOBILE=\"18514762028\"");
//        System.out.println(i);
    }
}
