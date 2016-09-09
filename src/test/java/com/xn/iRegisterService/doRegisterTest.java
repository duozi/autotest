package com.xn.iRegisterService;
/**
 * Created by Administrator on 2016/8/22.
 */

import cn.xn.user.domain.*;
import cn.xn.user.service.IRegisterService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring.xml"})
public class doRegisterTest extends TestCase{
    private static final Logger logger = LoggerFactory.getLogger(doRegisterTest.class);
    @Resource
    IRegisterService iRegisterService ;


    @Test
    public void test_doRegister() {
        RegisterReq registerReq = new RegisterReq();
		registerReq.setAppVersion("2.4.0");
		registerReq.setChannel("");
		registerReq.setEngine("");
		registerReq.setKeyword("");
		registerReq.setLoginName("18514762028");
		registerReq.setLoginPwd("123456");
		registerReq.setRefereeMobile("");
		registerReq.setRefereeUid("");
		registerReq.setSign("16d5e4005249234061e38129de2d1f9d");
		registerReq.setSourceType("android");
		registerReq.setSystemType("QGZ");


        CommonRlt<RegisterRlt> result = iRegisterService.doRegister(registerReq);
		logger.warn(result.toString());

    }

}