package com.xn.test.model;

import com.alibaba.dubbo.common.utils.ConfigUtils;

import com.xn.test.util.ReflectionUtils;
import org.apache.commons.lang.StringUtils;

import static com.xn.test.service.RunTestSuite.loader;


public class ServiceDesc {
    private String interfaceName;

    private String methodName;

    private String url;

    private String version;

    private String group;
    private String timeout;
    private  String appName;



    public ServiceDesc(String interfaceName, String methodName, String url, String version, String group, String timeout,String appName) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.url = url;
        this.version = version;
        this.group = group;
        this.timeout=timeout;
        this.appName=appName;
    }
    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
    public String getMethodName() {
        return methodName;
    }

    public String getVersion() {
        version = StringUtils.isBlank(this.version) ? ConfigUtils.getProperty("rpc.version", "1.0") : version;
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getClazz() {
        return interfaceName;
    }

    public Class getServiceClass() {
        return ReflectionUtils.loadClass(this.interfaceName,loader);
    }

    public String getGroup() {
        return this.group;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
