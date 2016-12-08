package com.xn.front.test.controller;/**
 * Created by xn056839 on 2016/12/3.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/get")
public class ReflectController {
    private static final Logger logger =  LoggerFactory.getLogger(ReflectController.class);

    @RequestMapping(value="queryIndex", method= RequestMethod.GET)
    @ResponseBody
    public String getMethod(String interfaceName){
        String result="{\"result\":[\"abc\",\"asd\"]}";
        return result;

    }

    @RequestMapping(value="index", method= RequestMethod.GET)
    public ModelAndView getView(String interfaceName){
        ModelAndView m = new ModelAndView("index");
        return m;

    }
}
