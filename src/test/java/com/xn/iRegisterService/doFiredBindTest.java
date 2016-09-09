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
public class doFiredBindTest extends TestCase{
    private static final Logger logger = LoggerFactory.getLogger(doFiredBindTest.class);
    @Resource
    IRegisterService iRegisterService ;


    @Test
    public void test_doFiredBind() {
        FiredBindReq firedBindReq = new FiredBindReq();
		firedBindReq.setAppVersion("2.4.0");
        firedBindReq.setfUnionid("ozbuTt6mNl566ITM6O1111111111");
		firedBindReq.setMemberNo("18514762028");
		firedBindReq.setSign("43d08f6d0cbd82e0efd5fc07580b885d");
		firedBindReq.setSourceType("android");
		firedBindReq.setSysSource("wechat");
		firedBindReq.setSystemType("QGZ");



        CommonRlt<EmptyObject> result = iRegisterService.doFiredBind(firedBindReq);
        logger.warn(result.toString());
        assertEquals("成功",result.getReturnMsg());
    }

}