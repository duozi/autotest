package com.xn.test.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.alibaba.fastjson.JSON;
import com.xn.test.Exception.AssertNotEqualException;
import com.xn.test.response.Assert;
import com.xn.test.model.KeyValueStore;
import com.xn.test.response.AssertItem;
import com.xn.test.response.Response;
import com.xn.test.result.Report;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AssertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AssertCommand.class);
    private final static String separator = System.getProperty("line.separator", "\r\n");

    private Command paramAssertCommand;
    public Assert assertItem;
    private List<Command> redisAssertCommandList;
    private List<Command> DBAssertCommandList;

    public void setAssertItem(String request, Response response, String result) {
        assertItem.setRequest(request);
        assertItem.setResponse(response);
        assertItem.setResult(result);
    }

    public void setParamAssertCommand(ParaAssertCommand paramAssertCommand) {
        this.paramAssertCommand = paramAssertCommand;
    }

    public void setRedisAssertCommand(List<Command> redisAssertCommand) {
        this.redisAssertCommandList = redisAssertCommand;
    }

    public void setDBAssertCommand(List<Command> DBAssertCommand) {
        this.DBAssertCommandList = DBAssertCommand;
    }

    public AssertCommand(Command paramAssertCommand, List<Command> redisAssertCommandList, List<Command> DBAssertCommandList, Assert assertItem) {
        this.paramAssertCommand = paramAssertCommand;
        this.assertItem = assertItem;
        this.redisAssertCommandList = redisAssertCommandList;
        this.DBAssertCommandList = DBAssertCommandList;
    }
    public static Map<String, String> convertKeyValueStoreToMap(List<KeyValueStore> params) {
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
    public static void deepAssert(JSONObject jsonObject, String key, String value, Assert assertItem) throws AssertNotEqualException {
        if (key.contains(".")) {
            String[] array = key.split("\\.", 2);
            if (jsonObject.containsKey(array[0])) {

                deepAssert(jsonObject.getJSONObject(array[0]), array[1], value, assertItem);


            } else {
                Report.failedPlus();
                AssertItem item = new AssertItem(key, value, "检验字段不存在");
                assertItem.addDiff(item);

                throw new AssertNotEqualException("assert is not Equal");
            }
        } else {
            if (jsonObject.containsKey(key)) {
                String returnValue = String.valueOf(jsonObject.get(key));
                if (!returnValue.equals(value)) {
                    Report.failedPlus();
//                int error=Report.getReport().getError();
//                int failed=Report.getReport().getFailed();
                    AssertItem item = new AssertItem(key, value, returnValue);
                    assertItem.addDiff(item);
//                    Report.getReport().assertAdd(assertItem);
                    throw new AssertNotEqualException("assert is not Equal");

                }
            } else {
                Report.failedPlus();
                AssertItem item = new AssertItem(key, value, "检验字段不存在");
                assertItem.addDiff(item);
//                Report.getReport().assertAdd(assertItem);
                throw new AssertNotEqualException("assert is not Equal");
            }


        }
    }

    @Override
    public void execute() {
        try {
            if (paramAssertCommand != null) {
                paramAssertCommand.executeWithException();
            }
            if (DBAssertCommandList != null && DBAssertCommandList.size() > 0) {
                for (Command command : DBAssertCommandList) {
                    command.executeWithException();
                }
            }
            if (redisAssertCommandList != null && redisAssertCommandList.size() > 0) {
                for (Command command : redisAssertCommandList) {
                    command.executeWithException();
                }


            }
        } catch (Exception e) {
            logger.error("assert error");
        }finally {
            Report.getReport().assertAdd(assertItem);
        }

    }

    @Override
    public void executeWithException() throws Exception {

    }
}
