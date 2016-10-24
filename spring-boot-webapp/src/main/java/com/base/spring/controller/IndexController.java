package com.base.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class IndexController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);


    /**
     * 默认首页，直接跳转到相关的 Controller
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        logger.info("redirect to other controller");
       // return "redirect:http://canhelp.cn";
         return "redirect:/menu/ajax/index.html";  // 跳转到 AceAdminAjaxMenuController 中的 url
    }
}