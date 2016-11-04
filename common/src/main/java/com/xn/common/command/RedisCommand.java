package com.xn.common.command;/**
 * Created by xn056839 on 2016/9/12.
 */


import com.xn.common.util.RedisUtil;
import com.xn.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class RedisCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(RedisCommand.class);
    public static final String PREFIX_LOGINPWD = "loginPwd";
    private String systemType;
    private String loginName;
    private int times;
    private String methodName;
    private String sourceType;
    private String memberNo;
    private String tokenId;
    private String key;
    private String value;
    private int time;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        if(value.contains("getTime")){
            value=value.replace("getTime",String.valueOf(new Date().getTime()));
        }

        this.value = value;
        System.out.println("--------"+this.value);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getTime() {
        return time;
    }

    RedisUtil redisUtil = new RedisUtil();

    /**
     * 从redis中获取登录错误次数
     */
    private int getErrorTimes() {
        try {
            int errorTime = redisUtil.getErrorTime(PREFIX_LOGINPWD + "-" + systemType + "-" + loginName);
            logger.info("错误次数为---------------" + errorTime);
            return errorTime;
        } catch (Exception e1) {
            logger.error(String.format("~~~[%s]缓存中获取登录密码错误次数异常~~~", loginName), e1);
        }
        return 0;
    }

    /**
     * 把用户登录密码错误次数存入redis
     */
    private void saveErrorTime() {
        String key = PREFIX_LOGINPWD + "-" + systemType + "-" + loginName;
        try {
            redisUtil.putErrorTime(key, times, "login");
        } catch (Exception e) {
            logger.error(String.format("~~~~用户名:[%s],密码错误次数存入redis中报错：[%s]~~~~", loginName, e));
            e.printStackTrace();
        }


    }

//    private void logoutDelToken() {
//        try {
//            redisUtil.logout(systemType, sourceType, tokenId, memberNo);
//        } catch (Exception e) {
//            logger.error("delete logout error");
//            e.printStackTrace();
//        }
//    }

//    private void loginSaveToken() {
//        redisUtil.saveToken2Redis(systemType, sourceType, loginName, tokenId, memberNo);
//    }

    private void set(String key,String value,int time) {
        if(StringUtil.isEmpty(key)||StringUtil.isEmpty(value)){
            return ;
        }
        redisUtil.set(key,value,time);
    }

    private String get(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        return redisUtil.get(key);
    }
    private  void del(String key){
        redisUtil.del(key);
    }
    @Override
    public void execute() {
        if (methodName.equalsIgnoreCase("saveErrorTime")) {
            saveErrorTime();
        } else if (methodName.equalsIgnoreCase("getErrorTimes")) {
            getErrorTimes();
        } else if (methodName.equalsIgnoreCase("loginSaveToken")) {
//            loginSaveToken();
        } else if (methodName.equalsIgnoreCase("logoutDelToken")) {
//            logoutDelToken();
        }else if (methodName.equalsIgnoreCase("set")) {
            set(key,value,time);
        }else if (methodName.equalsIgnoreCase("del")) {
            del(key);
        }
    }

//    @Override
//    public void executeWithException() throws Exception {
//
//    }

    public static void main(String[] args) {
        RedisCommand redisCommand=new RedisCommand();
        redisCommand.setKey("UNIUSER-login-QGZ-77683f51-da44-4f2d-8120-e009ef3bf351");
        redisCommand.setValue("[{\"bid\":\"UNIUSER\",\"date\":1474513018036,\"source\":\"app\",\"tokenId\":\"77683f51-da44-4f2d-8120-e009ef3bf351\",\"uid\":\"login-QGZ-77683f51-da44-4f2d-8120-e009ef3bf351\"}]");
        redisCommand.del(redisCommand.getKey());
    }
}
