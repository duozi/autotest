package com.xn.common.command;

/**
 * Created by xn056839 on 2016/8/31.
 */
public interface Command {

     void execute();

     void executeWithException() throws Exception;
}
