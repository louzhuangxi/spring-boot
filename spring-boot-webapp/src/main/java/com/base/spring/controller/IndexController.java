package com.base.spring.controller;

import org.h819.web.commons.MyServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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
        return "redirect:/menu/ajax/index.html";  // 跳转到 AceAdminAjaxMenuController 中的 url
    }


    /**
     * 不用 ajax 方式，直接演示 ztree 用法
     * 跳转到 AceAdminAjaxMenuController 中的 url
     * @return
     */
    @RequestMapping(value = "/ztree/example", method = RequestMethod.GET)
    public String ztree(HttpServletRequest request) {
        logger.info("非 ajax 方式，演示 ztree 用法");
        logger.info("request path={} ,  will go to /html/ajax/content/menu-tree.ftl", MyServletUtils.getFullPath(request));
        return "admin/ace/html/ajax/ztree-example";
    }
}