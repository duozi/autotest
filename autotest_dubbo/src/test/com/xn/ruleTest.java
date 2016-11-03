package com.xn;/**
 * Created by xn056839 on 2016/10/17.
 */

import com.xiaoniu.dataplatform.ruleengine.RuleReq;
import com.xiaoniu.dataplatform.ruleengine.RuleResp;
import com.xiaoniu.dataplatform.ruleengine.service.IRuleEngineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class ruleTest {
    private static final Logger logger = LoggerFactory.getLogger(ruleTest.class);
    @Resource
    IRuleEngineService iRuleEngineService;
    @Test
    public  void ruleTest(){
        RuleReq req=new RuleReq();
        req.setAppId("credit-ndf");
        req.setAccessToken("012346");
//        req.setInputMap();
        req.setSignature("nin");
        RuleResp rule= iRuleEngineService.syncHandler(req);
        System.out.println(rule);

    }

}
