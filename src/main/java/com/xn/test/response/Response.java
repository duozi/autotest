/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.xn.test.response;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xn.test.Exception.AssertNotEqualException;
import com.xn.test.fastjson.TestDoubleSerializer;
import com.xn.test.result.Report;
import com.xn.test.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.hamcrest.core.Is;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

import static com.xn.test.command.AssertCommand.deepAssert;
import static org.junit.Assert.assertThat;

public class Response {
    private final static Logger logger = LoggerFactory.getLogger(Response.class);

    Object body;
    Throwable exception;

    public Response() {
    }

    public static Logger getLogger() {
        return logger;
    }

    public Response(Object body, Throwable exception) {
        this.exception = exception;
        this.body = body;
    }


    private boolean isJson(String json) {
        try {
            JSONObject.fromObject(json);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void paraVerify(Map<String, String> expected, Assert assertItem) throws AssertNotEqualException {

        try {
            validate(expected, assertItem);
        } catch (AssertNotEqualException e) {
            assertItem.setResult("failed");
            logger.error("parameter assert is  not Equal");

        }



    }



//    private void assertBody(String expected) {
//        if (expected != null) {
//            String json = bodyInString(body);
//            assertString(json, expected);
//            validate(expected, json);
//        }
//    }

    private void validate(Map<String, String> expected, Assert assertItem) throws AssertNotEqualException {
        if (body != null) {

            JSONObject jsonObject = JSONObject.fromObject(body);
            for (String key : expected.keySet()) {
                String value = expected.get(key);
                deepAssert(jsonObject, key, value, assertItem);
            }
        }
        if (exception != null) {
            assertItem.addException(exception);

        }
    }


    private void assertString(String actual, String expected) {
        if (!isJson(actual)) {
            try {
                assertThat("接口返回的是字符串(非json)，使用字符串对比失败",
                        StringUtils.trim(actual),
                        Is.is(StringUtils.trim(expected)));
            } catch (AssertionError e) {
                assertThat("接口返回的是字符串(非json)，使用字符串对比失败",
                        String.format("\"%s\"", StringUtils.trim(actual)),
                        Is.is(StringUtils.trim(expected)));
            }
        }
    }

    private String bodyInString(Object body) {
        if (body instanceof String)
            return body.toString();
        return responseJson(body);
    }

    private String responseJson(Object body) {
        Boolean jsonWriteOriginalDoubleValue = Boolean.valueOf(StringUtil.getPro("json_write_original_double_value", "false"));
        SerializeConfig config = new SerializeConfig();
        if (jsonWriteOriginalDoubleValue) {
            config.setAsmEnable(false);
            config.put(Double.class, TestDoubleSerializer.INSTANCE);
        }
        return JSON.toJSONString(body, config, SerializerFeature.WriteMapNullValue);
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error(String.format("class <%s> not found.", className), e);
            throw new AssertionError(e);
        }
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getBody() {
        return this.body;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "Response{" + "body='" + body + '\'' + ", exception=" + exception + '}';
    }
}
