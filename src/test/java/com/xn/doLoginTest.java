package com.xn; /**
 * Created by Administrator on 2016/8/22.
 */

import cn.xn.user.domain.CommonRlt;
import cn.xn.user.domain.LoginReq;
import cn.xn.user.domain.LoginRlt;
import cn.xn.user.service.ILoginService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.xn.test.util.StringUtil;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring.xml"})
public class doLoginTest extends TestCase{
    private static final Logger logger = LoggerFactory.getLogger(doLoginTest.class);
    @Resource
    ILoginService iLoginService ;


    @Test
    public void test_doLogin() {
        LoginReq loginReq = new LoginReq();

        loginReq.setSystemType("QGZ");
		loginReq.setLoginName("13111111111");
		loginReq.setLoginPwd("123456");
        loginReq.setAppVersion("2.4.0");
		loginReq.setSourceType("android");


//        loginReq.setSign(StringUtil.addSign(loginReq));
        CommonRlt<LoginRlt> result = iLoginService.doLogin(loginReq);

        logger.warn(result.toString());

    }

}