package com.xn;

import cn.xn.user.domain.*;
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
public class oldDataChangePwdTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(oldDataChangePwdTest.class);

    @Resource
    IRegisterService iRegisterService;
    @Resource
    ILoginService iLoginService;
    @Resource
    IPwdService iPwdService;

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

        String deleteLoginName = "delete from t_customer where member_no=\"8880d44d-d907-4e03-bd95-100cfc7b999\" and login_name=\"99999999444\" and system_type=\"QGZ\"";
        DBUtil.updateDB(deleteLoginName);
        String insert = "insert into t_customer (member_no,login_name,login_pwd,member_name,mobile,system_type,member_level,status)values (\"8880d44d-d907-4e03-bd95-100cfc7b999\", \"99999999444\", \"fEqNCco3Yq9h5ZUglD3CZJT4lBs=\", \"33\", \"99999999444\", \"QGZ\",1,1)";
        DBUtil.updateDB(insert);
        LoginReq loginReq = new LoginReq();
        loginReq.setAppVersion("2.4.0");
        loginReq.setSourceType("android");
        loginReq.setSystemType("QGZ");
        loginReq.setLoginName("99999999444");
        loginReq.setLoginPwd("123456");
        loginReq.setSign(StringUtil.setSign(loginReq));
        logger.warn("loginReq------" + loginReq);
        CommonRlt<LoginRlt> loginRltCommonRlt = iLoginService.doLogin(loginReq);
        logger.warn("登陆" + loginRltCommonRlt.toString());
        String memberNo = loginRltCommonRlt.getData().getMemberNo();
        String tokenId = loginRltCommonRlt.getData().getTokenId();


        ModifyLoginPwdReq modifyLoginPwdReq = new ModifyLoginPwdReq();
        modifyLoginPwdReq.setAppVersion("2.4.0");
        modifyLoginPwdReq.setSourceType("android");
        modifyLoginPwdReq.setSystemType("QGZ");
        modifyLoginPwdReq.setMemberNo(memberNo);
        modifyLoginPwdReq.setLoginPwd("123456");
        modifyLoginPwdReq.setLoginNewPwd("666666");
        modifyLoginPwdReq.setTokenId(tokenId);
        modifyLoginPwdReq.setSign(StringUtil.setSign(modifyLoginPwdReq));
        logger.warn("modify----" + modifyLoginPwdReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt = iPwdService.doModifyLoginPwd(modifyLoginPwdReq);
        logger.warn("修改密码" + emptyObjectCommonRlt.toString());

        LoginOutReq loginOutReq = new LoginOutReq();
        loginOutReq.setAppVersion("2.4.0");
        loginOutReq.setSourceType("android");
        loginOutReq.setSystemType("QGZ");
        loginOutReq.setTokenId(tokenId);
        loginOutReq.setMemberNo(memberNo);
        loginOutReq.setSign(StringUtil.setSign(loginOutReq));
        logger.warn("logout----" + loginOutReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt1 = iLoginService.doLoginOut(loginOutReq);
        logger.warn("登出" + emptyObjectCommonRlt1.toString());


    }

}