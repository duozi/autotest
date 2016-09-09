package com.xn.register;

/**
 * Created by xn056839 on 2016/8/29.
 */

import cn.xn.user.domain.CheckMobileReq;
import cn.xn.user.domain.CommonRlt;
import cn.xn.user.domain.RegisterReq;
import cn.xn.user.domain.RegisterRlt;
import cn.xn.user.service.IRegisterService;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.xn.test.service.DBService;
import com.xn.test.util.StringUtil;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class registerTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(com.xn.iRegisterService.doRegisterTest.class);
    private static String loginName = StringUtil.addLoginName();
    @Resource
    IRegisterService iRegisterService;


    @BeforeClass
    public static void beforeClass() {

        logger.warn("loginName:" + loginName);
        /**
         * 注册之前先确定之前没有注册
         */
        DBService.newDB();
        String count = DBService.getCountFromDB("select count(*) from customer_info where login_name=" + loginName);
        while (!count.equals("0")) {
            loginName = StringUtil.addLoginName();
            count = DBService.getCountFromDB("select count(*) from customer_info where login_name=" + loginName);
        }
    }

    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @AfterClass
    public static void closeDB() {
        /**
         * 关闭数据库连接
         */
        logger.warn("over");
        DBService.closeDB();
    }

    @Test
    public void test_doRegister() {

        /**
         *给接口赋值
         */
        RegisterReq registerReq = new RegisterReq();
        registerReq.setAppVersion("2.4.0");
        registerReq.setChannel("");
        registerReq.setEngine("");
        registerReq.setKeyword("");
        registerReq.setLoginName(loginName);
        registerReq.setLoginPwd("123456");
        registerReq.setRefereeMobile("");
        registerReq.setRefereeUid("");
        registerReq.setSourceType("android");
        registerReq.setSystemType("QGZ");
        registerReq.setSign(StringUtil.addSign(registerReq));

        CommonRlt<RegisterRlt> result = iRegisterService.doRegister(registerReq);
        logger.warn(result.toString());
        assertEquals(result.getReturnMsg().toString(), "0");
        //确保在数据库中插入数据
        String count = DBService.getCountFromDB("select count(*) from customer_info where login_name=" + loginName);
        logger.warn("count-----" + count);
        assertEquals(count, "1");

    }

    @Ignore
    @Test
    public void test_doCheckMobile() {
        logger.warn("log" + loginName);
        CheckMobileReq checkMobileReq = new CheckMobileReq();
        checkMobileReq.setAppVersion("2.4.0");
        checkMobileReq.setLoginName(loginName);
        checkMobileReq.setSourceType("android");
        checkMobileReq.setSystemType("QGZ");
        checkMobileReq.setSign(StringUtil.addSign(checkMobileReq));

        CommonRlt<Boolean> result = iRegisterService.doCheckMobile(checkMobileReq);
        logger.warn(result.toString());
        assertEquals(result.getReturnCode().toString(), "0");
        assertEquals(result.getData().toString(), "true");
    }

}

