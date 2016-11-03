package com.xn;

import cn.xn.user.domain.*;
import cn.xn.user.service.ILoginService;

import cn.xn.user.service.IPwdService;
import cn.xn.user.service.IRegisterService;

import com.xn.test.*;

import com.xn.test.util.StringUtil;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class changePwdTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(changePwdTest.class);

    @Resource
    IRegisterService iRegisterService;
    @Resource
    ILoginService iLoginService;
    @Resource
    IPwdService iPwdService;

//    @BeforeClass
//    public static void beforeClass() {
//        DBUtil.newDB();
//
//    }

//    @AfterClass
//    public static void afterClass() {
//        DBUtil.closeDB();
//
//    }

    @Test
    public void changePwdTest() {
//        String deleteLoginName = "delete from t_customer where login_name=\"55555555555\" and system_type=\"QGZ\"";
//        DBUtil.updateDB(deleteLoginName);
        RegisterReq registerReq = new RegisterReq();
        registerReq.setAppVersion("2.4.0");
        registerReq.setSourceType("android");
        registerReq.setSystemType("QGZ");
        registerReq.setLoginName("55555555555");
        registerReq.setLoginPwd("555555");
        registerReq.setSign(StringUtil.setSign(registerReq));
        logger.info("registerReq----" + registerReq.toString());
        CommonRlt<RegisterRlt> registerRltCommonRlt = iRegisterService.doRegister(registerReq);
        logger.info("注册" + registerRltCommonRlt.toString());


        LoginReq loginReq = new LoginReq();
        loginReq.setAppVersion("2.4.0");
        loginReq.setSourceType("android");
        loginReq.setSystemType("QGZ");
        loginReq.setLoginName("55555555555");
        loginReq.setLoginPwd("555555");
        loginReq.setSign(StringUtil.setSign(loginReq));
        logger.info("loginReq------" + loginReq);
        CommonRlt<LoginRlt> loginRltCommonRlt = iLoginService.doLogin(loginReq);
        logger.info("登陆" + loginRltCommonRlt.toString());
        String memberNo = loginRltCommonRlt.getData().getMemberNo();
        String tokenId = loginRltCommonRlt.getData().getTokenId();


        ModifyLoginPwdReq modifyLoginPwdReq = new ModifyLoginPwdReq();
        modifyLoginPwdReq.setAppVersion("2.4.0");
        modifyLoginPwdReq.setSourceType("android");
        modifyLoginPwdReq.setSystemType("QGZ");
        modifyLoginPwdReq.setMemberNo(memberNo);
        modifyLoginPwdReq.setLoginPwd("555555");
        modifyLoginPwdReq.setLoginNewPwd("666666");
        modifyLoginPwdReq.setTokenId(tokenId);
        modifyLoginPwdReq.setSign(StringUtil.setSign(modifyLoginPwdReq));
        logger.info("modify----" + modifyLoginPwdReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt = iPwdService.doModifyLoginPwd(modifyLoginPwdReq);
        logger.info("修改密码" + emptyObjectCommonRlt.toString());

        LoginOutReq loginOutReq=new LoginOutReq();
        loginOutReq.setAppVersion("2.4.0");
        loginOutReq.setSourceType("android");
        loginOutReq.setSystemType("QGZ");
        loginOutReq.setTokenId(tokenId);
        loginOutReq.setMemberNo(memberNo);
        loginOutReq.setSign(StringUtil.setSign(loginOutReq));
        logger.info("logout----" + loginOutReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt1=iLoginService.doLoginOut(loginOutReq);
        logger.info("登出"+emptyObjectCommonRlt1.toString());




//        DBUtil.updateDB(deleteLoginName);
    }

}