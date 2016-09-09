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
public class doBindTest extends TestCase{
    private static final Logger logger = LoggerFactory.getLogger(doBindTest.class);
    @Resource
    IRegisterService iRegisterService ;


    @Test
    public void test_doBind() {
        BindReq bindReq = new BindReq();
		bindReq.setAppVersion("2.4.0");
//		bindReq.setExpireData();
		bindReq.setfName("wechat");
		bindReq.setfToken("");
		bindReq.setfUnionid("ozbuTt6mNl566ITM6O1111111111");
		bindReq.setMemberNo("18514762028");
		bindReq.setSign("53b65b5c0e4516c0903655c4efc7761d");
		bindReq.setSourceType("android");
		bindReq.setSysSource("wechat");
		bindReq.setSystemType("QGZ");


        CommonRlt<EmptyObject> result = iRegisterService.doBind(bindReq);
        logger.warn(result.toString());
		assertEquals("",result.getReturnMsg());
    }

}