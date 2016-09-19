package com.xn.test.util;

import com.mysql.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by xn056839 on 2016/8/30.
 */

public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;

    public static void newDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = StringUtil.getPro("jdbc.properties", "jdbc_url");
            String user = StringUtil.getPro("jdbc.properties", "jdbc_username");
            String pwd = StringUtil.getPro("jdbc.properties", "jdbc_password");
            con = (Connection) DriverManager.getConnection(url, user, pwd);
            stmt = con.createStatement();
            logger.warn("new DB connection");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeDB() {
        try {
//            rs.close();
            //关闭语句
            stmt.close();
            //关闭连接
            con.close();
            logger.warn("close DB connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ResultSet selectFromDB(String sql) {
        try {
            logger.warn("execute sql:{}",sql);
            rs = stmt.executeQuery(sql);
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
        String count = "";
        try {
            logger.warn("execute sql:{}",sql);
            rs = stmt.executeQuery(sql);

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

    public static int  updateDB(String sql) {
        int rs = -1;
        try {
            logger.warn("execute sql:{}",sql);
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
