package com.xn.test.service;/**
 * Created by xn056839 on 2016/9/2.
 */

import com.xn.test.command.*;
import com.xn.test.response.Assert;
import com.xn.test.model.KeyValueStore;
import com.xn.test.model.ServiceDesc;
import com.xn.test.response.Response;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CreateCommand {
    private static final Logger logger = LoggerFactory.getLogger(CreateCommand.class);

    public Command createDBCommand(String sql) {

        return new DBCommand(sql);
    }

    public Command createNewDBCommand() {
        return new NewDBCommand();
    }

    public Command createCloseDBCommand() {
        return new CloseDBCommand();
    }
    public Command createCaseCommand(List<KeyValueStore> params,ServiceDesc serviceDesc,String casePath) {

        return new CaseCommand(params, serviceDesc,casePath);
    }
    public Command createAssertCommand(Response response, List<KeyValueStore> processedParams, Assert assertItem) {

        return new AssertCommand(response,processedParams,assertItem);
    }


}
