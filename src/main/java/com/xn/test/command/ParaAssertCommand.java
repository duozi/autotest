package com.xn.test.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.test.Exception.AssertNotEqualException;
import com.xn.test.model.KeyValueStore;
import com.xn.test.response.Assert;
import com.xn.test.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xn.test.command.AssertCommand.convertKeyValueStoreToMap;


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
        Map<String, String> expectation = convertKeyValueStoreToMap(processedParams);
        try {
            logger.warn("para assert command<{}> is starting... ", expectation);
            preResult.paraVerify(expectation, assertItem);

        } catch (AssertNotEqualException e) {
            String message = "assert para step invoke has error,expect=" + expectation + separator + "result=" + preResult;
            logger.error(message, e);
            throw e;
        }
    }



    @Override
    public void execute() {
    }

    @Override
    public void executeWithException() throws AssertNotEqualException {
        doExecuteParaAssert(assertItem.getResponse(), processedParams);
    }


}
