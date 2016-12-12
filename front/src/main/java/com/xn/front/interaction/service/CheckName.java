package com.xn.front.interaction.service;/**
 * Created by xn056839 on 2016/12/8.
 */

import com.xn.common.model.ServiceDesc;
import com.xn.front.interaction.dao.PropertiesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckName {
    private static final Logger logger = LoggerFactory.getLogger(CheckName.class);

    @Autowired
    public PropertiesDao propertiesDao;
    public void setDubboServiceDesc(ServiceDesc s){
        try {
            propertiesDao.createServiceDesc(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ServiceDesc getDubboServiceDesc(String group){
        try {
            return propertiesDao.getServiceDesc(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
