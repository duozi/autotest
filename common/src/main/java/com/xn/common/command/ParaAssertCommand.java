package com.xn.common.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.common.Exception.AssertNotEqualException;
import com.xn.common.response.Assert;
import com.xn.common.response.Response;
import com.xn.common.model.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class ParaAssertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ParaAssertCommand.class);
    private final static String separator = System.getProperty("line.separator", "\r\n");

    private List<KeyValueStore> processedParams;

    private Assert assertItem;

    public ParaAssertCommand(List<KeyValueStore> processedParams) {
        this.processedParams = processedParams;
    }

    public void setAssertItem(Assert assertItem) {
        this.assertItem = assertItem;
    }

    protected void doExecuteParaAssert(Response preResult, List<KeyValueStore> processedParams) throws AssertNotEqualException {
        Map<String, String> expectation = AssertCommand.convertKeyValueStoreToMap(processedParams);
        try {

            preResult.paraVerify(expectation, assertItem);

        } catch (AssertNotEqualException e) {
            String message = "assert para step invoke has error,expect=" + expectation + separator + "result=" + preResult;
            assertItem.setResult("failed");
            logger.error(message,e);
            throw e;
        }
    }



    @Override
    public void execute() throws AssertNotEqualException {
        doExecuteParaAssert(assertItem.getResponse(), processedParams);
    }

//    @Override
//    public void executeWithException() throws AssertNotEqualException {
//        doExecuteParaAssert(assertItem.getResponse(), processedParams);
//    }


}
