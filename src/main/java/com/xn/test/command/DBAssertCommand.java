package com.xn.test.command;
/**
 * Created by xn056839 on 2016/9/5.
 */

import com.xn.test.Exception.AssertNotEqualException;
import com.xn.test.model.KeyValueStore;
import com.xn.test.response.Assert;
import com.xn.test.response.AssertItem;
import com.xn.test.util.DBUtil;
import com.xn.test.util.RedisUtil;
import com.xn.test.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

import static com.xn.test.command.AssertCommand.convertKeyValueStoreToMap;
import static com.xn.test.command.AssertCommand.deepAssert;


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

                logger.warn("DB assert getCount method command<{}> is starting... ", "sql=" + sql + ",count=" + expectCount);
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
    public void execute() {


    }

    @Override
    public void executeWithException() throws Exception {
        doExecuteDBAssert();
    }


}
