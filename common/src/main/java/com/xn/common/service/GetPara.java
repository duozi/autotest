package com.xn.common.service;/**
 * Created by xn056839 on 2016/10/31.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;

public class GetPara {
    private static final Logger logger = LoggerFactory.getLogger(GetPara.class);


    private static URLClassLoader loader = null;
    private static String path;

    public static String getSystem() {
        return system;
    }

    public static void setSystem(String system) {
        GetPara.system = system;
    }

    private static String system;

    public URLClassLoader getLoader() {
        return loader;
    }

    public void setLoader(URLClassLoader loader) {
        this.loader = loader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
