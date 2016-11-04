package com.xn.common.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.common.response.Assert;
import com.xn.common.response.AssertItem;
import com.xn.common.Exception.AssertNotEqualException;
import com.xn.common.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBAssertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DBAssertCommand.class);
    private final static String separator = System.getProperty("line.separator", "\r\n");


    private Assert assertItem;
    private String sql;
    private String expectCount;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setExpectCount(String expectCount) {
        this.expectCount = expectCount;
    }

    public void setAssertItem(Assert assertItem) {
        this.assertItem = assertItem;
    }


    private void doExecuteDBAssert() throws AssertNotEqualException {
        if (sql != null) {
            String exact = "";

            try {

                exact = DBUtil.getCountFromDB(sql);

                logger.info("DB assert getCount method command<{}> is starting... ", "sql=" + sql + ",count=" + expectCount);
                DBVerify(exact);


            } catch (AssertNotEqualException e) {
                assertItem.setResult("failed");
                String message = "assert DB step invoke has error,expect count=" + expectCount + separator + "exact count=" + exact;
                logger.error(message, e);
                throw e;

            }
        }

    }

    private void DBVerify(String exactCount) throws AssertNotEqualException {

        if (!exactCount.equals(expectCount)) {
            AssertItem item = new AssertItem("DB.getCount", expectCount, exactCount);
            assertItem.addDiff(item);
            throw new AssertNotEqualException("assert is not Equal");
        }
    }


    @Override
    public void execute() throws AssertNotEqualException {
        doExecuteDBAssert();

    }

    @Override
    public void executeWithException() throws Exception {
        doExecuteDBAssert();
    }


}
