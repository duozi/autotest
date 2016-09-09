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
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class doCheckMobileTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(doCheckMobileTest.class);
    @Resource
    IRegisterService iRegisterService;


    @Test
    public void test_doCheckMobile() {
        CheckMobileReq checkMobileReq = new CheckMobileReq();
        checkMobileReq.setAppVersion("2.4.0");
        checkMobileReq.setLoginName("18514762028");
        checkMobileReq.setSign("ca8c19f59f5c0108c8998facb9bc5352");
        checkMobileReq.setSourceType("android");
        checkMobileReq.setSystemType("QGZ");


        CommonRlt<Boolean> result = iRegisterService.doCheckMobile(checkMobileReq);
        logger.warn(result.toString());
        assertEquals(result.getData().toString(), "false");
    }

    @Test
    public void test_doCheckMobile_2() {
        CheckMobileReq checkMobileReq = new CheckMobileReq();
        checkMobileReq.setAppVersion("2.4.0");
        checkMobileReq.setLoginName("18514762029");
        checkMobileReq.setSign("97eab51619e79c11e1cc4f2c135b1db4");
        checkMobileReq.setSourceType("android");
        checkMobileReq.setSystemType("QGZ");


        CommonRlt<Boolean> result = iRegisterService.doCheckMobile(checkMobileReq);
        logger.warn(result.toString());
        assertEquals(result.getData().toString(), "true");
    }

}