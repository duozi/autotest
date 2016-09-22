package com.xn;

import cn.xn.user.domain.*;
import cn.xn.user.service.IFriendService;
import cn.xn.user.service.ILoginService;
import cn.xn.user.service.IPwdService;
import cn.xn.user.service.IRegisterService;
import com.xn.test.util.DBUtil;
import com.xn.test.util.StringUtil;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class friendLineTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(friendLineTest.class);

    @Resource
    IRegisterService iRegisterService;
    @Resource
    ILoginService iLoginService;
    @Resource
    IFriendService iFriendService;

    @BeforeClass
    public static void beforeClass() {
        DBUtil.newDB();

    }

    @AfterClass
    public static void afterClass() {
        DBUtil.closeDB();

    }

    @Test
    public void changePwdTest() {
        String deleteLoginName = "delete from t_customer where login_name=\"66666666666\" and system_type=\"QGZ\"";
        DBUtil.updateDB(deleteLoginName);
        RegisterReq registerReq = new RegisterReq();
        registerReq.setAppVersion("2.4.0");
        registerReq.setSourceType("android");
        registerReq.setSystemType("QGZ");
        registerReq.setLoginName("66666666666");
        registerReq.setLoginPwd("555555");
        registerReq.setSign(StringUtil.setSign(registerReq));
        logger.warn("registerReq----" + registerReq.toString());
        CommonRlt<RegisterRlt> registerRltCommonRlt = iRegisterService.doRegister(registerReq);
        logger.warn("注册" + registerRltCommonRlt.toString());


        LoginReq loginReq = new LoginReq();
        loginReq.setAppVersion("2.4.0");
        loginReq.setSourceType("android");
        loginReq.setSystemType("QGZ");
        loginReq.setLoginName("66666666666");
        loginReq.setLoginPwd("555555");
        loginReq.setSign(StringUtil.setSign(loginReq));
        logger.warn("loginReq------" + loginReq);
        CommonRlt<LoginRlt> loginRltCommonRlt = iLoginService.doLogin(loginReq);
        logger.warn("登陆" + loginRltCommonRlt.toString());
        String memberNo = loginRltCommonRlt.getData().getMemberNo();
        String tokenId = loginRltCommonRlt.getData().getTokenId();



        UpdateFriendReq updateFriendReq=new UpdateFriendReq();
        updateFriendReq.setAppVersion("2.4.0");
        updateFriendReq.setSourceType("android");
        updateFriendReq.setSystemType("QGZ");
        updateFriendReq.setMemberNo(memberNo);
        updateFriendReq.setMobile("1234567");
        updateFriendReq.setFriendJson("[{\"friendMemberNo\":\"e0a77f8e-92c3-447b-9196-0167f533e1bb\",\"friendMobile\":\"17777777777\",\"friendName\":\"测试88\",\"friendUserName\":\"\",\"isRegister\":\"Y\"}]");
        updateFriendReq.setSign(StringUtil.setSign(updateFriendReq));
        logger.warn("updateFriendReq-------"+updateFriendReq);
        CommonRlt<Boolean> booleanCommonRlt=iFriendService.updateFriend(updateFriendReq);
        logger.warn("更新朋友"+booleanCommonRlt.toString());




        LoginOutReq loginOutReq=new LoginOutReq();
        loginOutReq.setAppVersion("2.4.0");
        loginOutReq.setSourceType("android");
        loginOutReq.setSystemType("QGZ");
        loginOutReq.setTokenId(tokenId);
        loginOutReq.setMemberNo(memberNo);
        loginOutReq.setSign(StringUtil.setSign(loginOutReq));
        logger.warn("logout----" + loginOutReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt=iLoginService.doLoginOut(loginOutReq);
        logger.warn("登出"+emptyObjectCommonRlt.toString());




        DBUtil.updateDB(deleteLoginName);
    }

}