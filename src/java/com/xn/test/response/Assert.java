package com.xn.test.response;
/**
 * Created by xn056839 on 2016/9/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Assert {
    private static final Logger logger = LoggerFactory.getLogger(Assert.class);
    private String interfaceName;
    private String methodName;
    private String caseName;

    private AssertItem diff;
    private Throwable exception;


    public Assert(String interfaceName, String methodName, String caseName) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.caseName = caseName;
    }

    public Throwable getException() {
        return exception;
    }

    public void addDiff(AssertItem item) {
        this.diff=item;

    }
    public void addException(Throwable exception) {
        this.exception=exception;

    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getCaseName() {
        return caseName;
    }

    public AssertItem getDiff() {
        return diff;
    }
}

