package com.xn.common.command;
/**
 * Created by xn056839 on 2016/8/31.
 */

import com.xn.common.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DBCommand.class);
    private String sql;
    private String name;

    public DBCommand(String name,String sql) {
        this.name=name;
        this.sql = sql;
    }

    @Override
    public void execute() {
        if (sql.toLowerCase().startsWith("select")) {
            DBUtil.selectFromDB(name,sql);
        }else{
            DBUtil.updateDB(name,sql);
        }
    }

    @Override
    public void executeWithException() throws Exception {

    }
}
