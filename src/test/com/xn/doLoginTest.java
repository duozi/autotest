package com.xn;

import cn.xn.user.domain.*;
import cn.xn.user.service.ILoginService;

import com.xn.test.command.RedisCommand;

import com.xn.test.util.RedisUtil;
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
    @Resource
    RedisUtil redisUtil;

    @BeforeClass
    public static void beforeClass() {


        }

    @Before
    public void before() {
//		DBService.selectFromDB("select count(*) from customer_info where login_name=13111111111");
        RedisCommand redisCommand=new RedisCommand();
        redisCommand.setTimes(6);
        redisCommand.setLoginName("15942165834");
        redisCommand.setSystemType("ZC");
        redisCommand.setMethodName("saveErrorTime");
        redisCommand.execute();


    }

     @After
     public void after() {
//		DBService.updateDB("delete from customer_info where login_name=13111111111");

     }

     @AfterClass
     public static void afterClass() {
//		DBService.closeDB();

     }

    @Test
    public void test_demo_1() {
        LoginReq loginReq = new LoginReq();
		loginReq.setLoginName("15942165834");
		loginReq.setLoginPwd("1");
		loginReq.setSourceType("android");
		loginReq.setSystemType("ZC");
		loginReq.setAppVersion("2.4.0");
		loginReq.setSign("4594e167e827761d9d90e16fa61e22ea");

//        loginReq.setSign(StringUtil.addSign(loginReq));

        CommonRlt<LoginRlt> result = iLoginService.doLogin(loginReq);
        logger.warn(result.toString());
//		assertEquals(result.getReturnCode().toString(), "110003");
//		assertEquals(result.getReturnMsg().toString(), "登录名不存在");
    }
    @Ignore
    @Test
    public void test_demo_2() {
        LoginReq loginReq = new LoginReq();

//        loginReq.setSign(StringUtil.addSign(loginReq));

        CommonRlt<LoginRlt> result = iLoginService.doLogin(loginReq);
        logger.warn(result.toString());
    }



}