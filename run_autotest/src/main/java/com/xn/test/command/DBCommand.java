package com.xn.test.command;
/**
 * Created by xn056839 on 2016/8/31.
 */

import com.xn.test.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DBCommand.class);
    String sql;

    public DBCommand(String sql) {
        this.sql = sql;
    }

    @Override
    public void execute() {
        if (sql.toLowerCase().startsWith("select")) {
            DBUtil.selectFromDB(sql);
        }else{
            DBUtil.updateDB(sql);
        }
    }

    @Override
    public void executeWithException() throws Exception {

    }
}
