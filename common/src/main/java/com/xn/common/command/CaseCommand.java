package com.xn.common.command;/**
 * Created by xn056839 on 2016/10/31.
 */

import com.xn.common.response.Response;

public interface CaseCommand extends Command {

    Response response = new Response();
    String request = null;
    String casePath = null;

    @Override
    void execute() ;



    Response getResponse();

    String getResult();

    String getRequest();
}
