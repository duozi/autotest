package com.xn.front.interaction.controller;/**
 * Created by xn056839 on 2016/12/3.
 */


import com.xn.front.interaction.service.PlaneService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/new_plane")
public class PlaneController {
    private static final Logger logger = LoggerFactory.getLogger(PlaneController.class);
    @Autowired
    PlaneService planeService;

    @RequestMapping(value = "check_plane_name", method = RequestMethod.GET)
    @ResponseBody
    public String checkPlaneName(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "planeName", defaultValue = "World") String planeName) {
        logger.info("request parameter is :{}", planeName);
        JSONObject result = new JSONObject();
        boolean planeNameUsable=planeService.checkPlaneName(planeName);
        result.put("usable",planeNameUsable);
        return result.toString();

    }



}
