package com.xn.test.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.alibaba.fastjson.JSON;
import com.xn.test.Exception.AssertNotEqualException;
import com.xn.test.response.Assert;
import com.xn.test.model.KeyValueStore;
import com.xn.test.response.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AssertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AssertCommand.class);
    private final static String separator = System.getProperty("line.separator", "\r\n");
    private Response response;
    private List<KeyValueStore> processedParams;
    private Assert assertItem;
    private String request;

    public AssertCommand(Response response, List<KeyValueStore> processedParams, Assert assertItem) {
        this.response = response;
        this.processedParams = processedParams;
        this.assertItem = assertItem;
    }

    protected  void doExecuteInternal(Response preResult, List<KeyValueStore> processedParams)  {
        Map<String, String> expectation = convertKeyValueStoreToMap(processedParams);
        try {
            logger.warn("assert command<{}> is starting... ", expectation);
            preResult.verify(expectation,assertItem);

        } catch (AssertNotEqualException e) {
            String message = "assert step invoke has error,expect=" + expectation + separator + "result=" + preResult;
            logger.error(message, e);


        }
    }

    private Map<String, String> convertKeyValueStoreToMap(List<KeyValueStore> params) {
        Map<String, String> result = new HashMap<String, String>();
        for (KeyValueStore kvs : params) {
            Object value = kvs.getValue();
            if (value instanceof Map) {
                result.putAll((Map) value);
            } else {
                result.put(kvs.getName(), (String) value);
            }
        }
        return result;
    }

    @Override
    public void execute() {

        doExecuteInternal(response, processedParams);

    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
