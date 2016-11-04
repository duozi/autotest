package com.xn.common.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.common.Exception.AssertNotEqualException;
import com.xn.common.model.KeyValueStore;
import com.xn.common.response.Assert;
import com.xn.common.response.AssertItem;
import com.xn.common.util.RedisUtil;
import com.xn.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.xn.common.command.AssertCommand.convertKeyValueStoreToMap;
import static com.xn.common.command.AssertCommand.deepAssert;


public class RedisAssertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RedisAssertCommand.class);
    private final static String separator = System.getProperty("line.separator", "\r\n");

    private List<KeyValueStore> redisParams;
    private Assert assertItem;
    private String key;
    private String redisMethod;

    public void setKey(String key) {
        this.key = key;
    }

    public void setRedisMethod(String redisMethod) {
        this.redisMethod = redisMethod;
    }

    RedisUtil redisUtil = new RedisUtil();

    public void setRedisParams(List<KeyValueStore> redisParams) {
        this.redisParams = redisParams;
    }

    public void setAssertItem(Assert assertItem) {
        this.assertItem = assertItem;
    }

    private String getValue(String key) {
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        return redisUtil.get(key);
    }

    private Long getTime(String key) {
        if (StringUtil.isEmpty(key)) {
            return Long.valueOf(-3);
        }
        return redisUtil.getTime(key);
    }

    private void doExecuteRedisAssert(List<KeyValueStore> redisParams) throws AssertNotEqualException {
        if (redisParams != null && redisParams.size() > 0) {
            String exact = null;
            Map<String, String> expectation = convertKeyValueStoreToMap(redisParams);
            try {

                if (redisMethod.equalsIgnoreCase("getValue")) {
                    String redisValue = getValue(key);
                    exact=redisValue;
                    logger.info("redis assert getValue method command<{}> is starting... ", expectation);
                    redisVerifyValue(expectation,redisValue);
                } else if (redisMethod.equalsIgnoreCase("getTime")) {
                    Long time = getTime(key);
                    exact=String.valueOf(time);
                    logger.info("redis assert getTime method command<{}> is starting... ", expectation);
                    redisVerifyTime(expectation,time);
                }

            } catch (AssertNotEqualException e) {
                assertItem.setResult("failed");
                String message = "assert redis step invoke has error,expect=" + expectation + separator + "result=" + exact;
                logger.error(message, e);
                throw  e;

            }
        }

    }

    private void redisVerifyTime(Map<String, String> expectation,Long exactTime)throws AssertNotEqualException {


                Long time= Long.valueOf(expectation.get("time"));
                if(time!=exactTime){
                    AssertItem item = new AssertItem("redis.getTime", expectation.get("time"), String.valueOf(exactTime));
                    assertItem.addDiff(item);
                    throw new AssertNotEqualException("assert is not Equal");
                }
            }

    private void redisVerifyValue(Map<String, String> expected,String redisValue) throws AssertNotEqualException {
        if (redisValue != null) {
            JSONObject jsonObject = JSONObject.fromObject(redisValue);
            for (String key : expected.keySet()) {
                String value = expected.get(key);
                deepAssert(jsonObject, key, value, assertItem);
            }
        }


    }

    @Override
    public void execute() throws AssertNotEqualException {
        doExecuteRedisAssert(redisParams);

    }

//    @Override
//    public void executeWithException() throws Exception {
//        doExecuteRedisAssert(redisParams);
//    }


}
