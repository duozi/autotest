package com.xn.front.interaction.common;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 *  
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ResultSetInterceptor implements Interceptor {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object intercept(Invocation invocation) throws Throwable {
        Object obj = invocation.proceed();

        Pagenation p = PageInterceptor.getPage();
        if (p != null) {
            p.setResult((List) obj);
            return p;
        }

        return obj;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
