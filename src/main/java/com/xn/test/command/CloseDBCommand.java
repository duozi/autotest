package com.xn.test.command;/**
 * Created by xn056839 on 2016/8/31.
 */

import com.xn.test.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseDBCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CloseDBCommand.class);

    @Override
    public void execute() {
        DBUtil.closeDB();
    }

    @Override
    public void executeWithException() throws Exception {

    }
}
