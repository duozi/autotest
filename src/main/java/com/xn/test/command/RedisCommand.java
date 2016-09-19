package com.xn.test.command;/**
 * Created by xn056839 on 2016/9/12.
 */


import com.xn.test.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

public class RedisCommand implements Command {
    ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring.xml");
    private static final Logger logger = LoggerFactory.getLogger(RedisCommand.class);
    public static final String PREFIX_LOGINPWD = "loginPwd";
    private String systemType;
    private String loginName;
    private int times;
    private String methodName;
    private String sourceType;
    private String memberNo;
    private String tokenId;

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


    RedisUtil redisUtil = (RedisUtil) context.getBean("redisUtil");

    /**
     * 从redis中获取登录错误次数
     */
    private int getErrorTimes() {
        try {
            int errorTime = redisUtil.getErrorTime(PREFIX_LOGINPWD + "-" + systemType + "-" + loginName);
            logger.warn("错误次数为---------------" + errorTime);
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
    private void logoutDelToken() {
        try {
            redisUtil.logout(systemType,sourceType,tokenId,memberNo);
        } catch (Exception e) {
            logger.error("delete logout error");
            e.printStackTrace();
        }
    }
    private void loginSaveToken() {
        redisUtil.saveToken2Redis(systemType, sourceType, loginName, tokenId, memberNo);
    }

    @Override
    public void execute() {
        if (methodName.equalsIgnoreCase("saveErrorTime")) {
            saveErrorTime();
        } else if (methodName.equalsIgnoreCase("getErrorTimes")) {
            getErrorTimes();
        } else if (methodName.equalsIgnoreCase("loginSaveToken")) {
            loginSaveToken();
        }else if (methodName.equalsIgnoreCase("logoutDelToken")) {
            logoutDelToken();
        }
    }
}
