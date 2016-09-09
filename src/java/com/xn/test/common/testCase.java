//package xn.com.test.common;
//
//import junit.framework.TestCase;
//import junit.framework.TestResult;
//import junit.framework.TestSuites;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.command.context.ContextConfiguration;
//import org.springframework.command.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * Created by Administrator on 2016/8/25.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath*:spring.xml"})
//public class testCase extends TestCase{
//    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);
//    public static void suit(){
//        TestSuites suit=new TestSuites();
//        suit.addTestSuite(doLoginTest.class);
//        suit.addTestSuite(getAllFriendTest.class);
//
//        TestResult result=new TestResult();
//        suit.run(result);
//        logger.warn(result.toString());
//        return ;
//    }
//
//    public static void main(String[] args) {
//        suit();
//    }
//}
