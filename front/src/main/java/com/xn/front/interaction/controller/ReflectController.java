package com.xn.front.interaction.controller;/**
 * Created by xn056839 on 2016/12/3.
 */


import com.xn.common.model.ServiceDesc;
import com.xn.front.interaction.service.CheckName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/get")
public class ReflectController {
    private static final Logger logger = LoggerFactory.getLogger(ReflectController.class);
    @Autowired
    CheckName checkName;

    @RequestMapping(value = "queryIndex", method = RequestMethod.GET)
    @ResponseBody
    public String getMethod(String interfaceName) {
        String result = "{\"result\":[\"abc\",\"asd\"]}";
//        ServiceDesc serviceDesc = new ServiceDesc("asd", "ada", "adds", "adfdsf", "sfds", "adf", "adfd", "adfds", true);
        ServiceDesc serviceDesc= checkName.getDubboServiceDesc("adf");
        System.out.println(serviceDesc.getMethodName());
        System.out.println(serviceDesc.getUseZk());
        return result;

    }

//    @RequestMapping(value="index", method= RequestMethod.GET)
//    public ModelAndView getView(String interfaceName){
//        ModelAndView m = new ModelAndView("index");
//        return m;
//
//    }
}
