package com.xn;

import cn.xn.user.domain.*;
import cn.xn.user.service.ILoginService;
import cn.xn.user.service.IRegisterService;
import com.xn.test.service.impl.DBService;
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
@ContextConfiguration(locations={"classpath*:spring.xml"})
public class doLoginTest extends TestCase{
    private static final Logger logger = LoggerFactory.getLogger(doLoginTest.class);
    @Resource
    ILoginService iLoginService ;

    @BeforeClass
    public static void beforeClass() {
		DBService.newDB();

        }

    @Before
    public void before() {
		DBService.selectFromDB("select count(*) from customer_info where login_name=13111111111");

    }

     @After
     public void after() {
		DBService.updateDB("delete from customer_info where login_name=13111111111");

     }

     @AfterClass
     public static void afterClass() {
		DBService.closeDB();

     }

    @Test
    public void test_demo_1() {
        LoginReq loginReq = new LoginReq();
		loginReq.setLoginName("13111111111");
		loginReq.setLoginPwd("111111");
		loginReq.setSourceType("android");
		loginReq.setSystemType("QGZ");
		loginReq.setAppVersion("2.4.0");
		

        loginReq.setSign(StringUtil.addSign(loginReq));

        CommonRlt<LoginRlt> result = iLoginService.doLogin(loginReq);
        logger.warn(result.toString());
		assertEquals(result.getReturnCode().toString(), "110003");
		assertEquals(result.getReturnMsg().toString(), "登录名不存在");
    }
    @Test
    public void test_demo_2() {
        LoginReq loginReq = new LoginReq();

        loginReq.setSign(StringUtil.addSign(loginReq));

        CommonRlt<LoginRlt> result = iLoginService.doLogin(loginReq);
        logger.warn(result.toString());
    }



}