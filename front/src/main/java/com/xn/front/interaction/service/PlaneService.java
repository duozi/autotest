package com.xn.front.interaction.service;/**
 * Created by xn056839 on 2016/12/13.
 */

import com.xn.front.interaction.dao.PlaneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlaneService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);
    @Autowired
    PlaneDao planeDao;

    public boolean checkPlaneName(String name) {
        int count = planeDao.checkPlaneName(name);
        if (count==0) {
            return true;
        } else {
            return false;
        }
    }
}
