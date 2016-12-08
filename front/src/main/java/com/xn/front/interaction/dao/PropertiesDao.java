package com.xn.front.interaction.dao;

import com.xn.common.model.ServiceDesc;

import java.util.List;

/**
 * Created by xn056839 on 2016/12/8.
 */

public interface PropertiesDao {
    List<String> getAllPlaneName();
    void createServiceDesc(ServiceDesc serviceDesc) throws Exception;
}
