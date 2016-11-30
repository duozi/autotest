package com.xn.generate;/**
 * Created by xn056839 on 2016/11/2.
 */

import com.xn.common.mail.JavaMailWithAttachment;
import com.xn.common.service.GetPara;
import com.xn.common.util.JarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;

public class GenerateSuite {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSuite.class);


    //一般来说有六个参数，第一个是生成的类型，第二个是依赖的系统名称 第三个是suite写入的路径,第四个是收件人，第五个是如果生成Dubbo的服务名，第六个是如果生成Dubbo的依赖jar的地址，后两个在生成Dubbo的时候是必填的,否则只生成http
    public static void main(String[] args) {

        if (args.length > 0) {
//            String writePath = "d:/generate/";

            String type = args[0];
            String systemName=args[1];
            String writePath = args[2];
            if (type.equals("http")) {
                logger.info(" generate http suite file");

                GenerateHttpSuite generateHttpSuite = new GenerateHttpSuite();
                generateHttpSuite.getHttpSuite(writePath);
            } else if (type.equals("dubbo")) {
                logger.info("generate  dubbo suite file");

                String serviceName = args[4];
                String jarPath = args[5]+systemName+"/";


                URLClassLoader loader = JarUtil.addJar(jarPath);
                GetPara getPara = new GetPara();
                getPara.setLoader(loader);
                String[] services = serviceName.trim().split(",");
                GenerateDubboSuite generateDubboSuite = new GenerateDubboSuite();
                for (String s : services) {
                    generateDubboSuite.getDubboSuite(s, writePath);

                }

            }
            String zipOut = writePath + "suite.zip";
            FileZip fileZip = new FileZip();
            fileZip.zipFile(writePath + "suite", zipOut);
            JavaMailWithAttachment se = new JavaMailWithAttachment(true);
            se.doSendHtmlEmail("dubbo接口测试文件结构", "这是程序自动生成的接口测试文件目录结构，请下载附件", args[3], zipOut);
        }
    }
}
