package com.xn.common.command;
/**
 * Created by xn056839 on 2016/8/31.
 */


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.xn.common.model.KeyValueStore;
import com.xn.common.model.ServiceDesc;
import com.xn.common.objectfactory.BeanUtils;
import com.xn.common.response.Response;
import com.xn.common.result.Report;
import com.xn.common.util.FileUtil;
import com.xn.common.util.ReflectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;


public class DubboCaseCommand implements CaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(DubboCaseCommand.class);

    List<KeyValueStore> params;

    private final ServiceDesc serviceDesc;
    private Response response = new Response();
    private String request;
    private String casePath;
    private String result;


    public String getResult() {
        return result;
    }

    public Response getResponse() {
        return response;
    }

    public String getRequest() {
        return request;
    }

    public DubboCaseCommand(List<KeyValueStore> params, ServiceDesc serviceDesc, String casePath) {

        this.params = params;
        this.serviceDesc = serviceDesc;
        this.casePath = casePath;


    }

    private static String getRpcServiceUrl(String url) {
        StringBuilder sb = new StringBuilder("dubbo");
        sb.append("://");
        sb.append(url);
        if (sb.indexOf(":") == -1) {
            sb.append(":").append("20888");
        }
        return sb.toString();
    }

    public  Object getRpcService(ServiceDesc serviceDesc) throws  NoSuchMethodException, InvocationTargetException {
        String rpcUrl = getRpcServiceUrl(serviceDesc.getUrl());
        Class<?> interfaceClass = ReflectionUtils.loadClass(serviceDesc.getClazz());

        Object service = null;
        ApplicationConfig application = new ApplicationConfig();
        application.setName(serviceDesc.getAppName());
        ReferenceConfig<?> reference = new ReferenceConfig();
        reference.setApplication(application);
        reference.setInterface(interfaceClass);
        if (StringUtils.isNotBlank(serviceDesc.getVersion()) && !serviceDesc.getVersion().equals("*")) {
            reference.setVersion(serviceDesc.getVersion());
        }
        reference.setUrl(rpcUrl);
        if (StringUtils.isNotBlank(serviceDesc.getGroup())) {
            reference.setGroup(serviceDesc.getGroup());
        }
        reference.setTimeout(Integer.valueOf(serviceDesc.getTimeout()));
        service = reference.get();
//        reference.destroy();

        return service;
    }


    @Override
    public void execute()  {

        try {
            Object service = getRpcService(serviceDesc);
            Method executeMethod = ReflectionUtils.getMethod(serviceDesc.getMethodName(), serviceDesc.getServiceClass());
            Object[] parameters = BeanUtils.getParameters(params, executeMethod.getGenericParameterTypes());
            request = String.valueOf(JSON.toJSONString(parameters[0]));

                logger.info("Rpc request start: params={}", new Object[]{JSON.toJSONString(parameters)});

            Object result = executeMethod.invoke(service, parameters);

            response.setBody(result);
            FileUtil.fileWrite(casePath, "请求参数：" + request + "\n返回结果：" + response.toString());
            logger.info("Rpc response:{}", new Object[]{JSON.toJSONString(response)});
        } catch (InvocationTargetException ite) {
            logger.error("call rpc error", ite.getTargetException());
            Report.errorPlus();
            result = "error";
            response.setException(ite.getTargetException());

        }
//        catch (IllegalAccessException e) {
//            Report.errorPlus();
//            result = "error";
//            response.setException(e);
//            throw new RuntimeException("illegal access", e);
//        }
        catch (Exception e) {
            logger.error("call rpc error", e);
            response.setException(e);
            result = "error";
            Report.errorPlus();

        }

    }

    @Override
    public void executeWithException() throws Exception {

    }
}
