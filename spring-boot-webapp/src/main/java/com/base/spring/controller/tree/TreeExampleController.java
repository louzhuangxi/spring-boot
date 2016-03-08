package com.base.spring.controller.tree;

import org.h819.web.commons.MyServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 树管理页面
 */

@Controller
@RequestMapping("/tree/manage") //重要: 必须以 ajax 结尾，以符合 ace content_url 的要求
public class TreeExampleController {

    private static Logger logger = LoggerFactory.getLogger(TreeExampleController.class);

    @RequestMapping(value = "/ztree.html", method = RequestMethod.GET)
    public String ztree(HttpServletRequest request, Model model) {
        logger.info("go to ztree");
        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("menu_type", "Menu");
        return "admin/ace/html/ajax/ztree-example";
    }

    @RequestMapping(value = "/fuelux.html", method = RequestMethod.GET)
    public String fuelux() {
        logger.info("go to fueluxtree");
        return "admin/ace/html/ajax/fueluxtree-example";
    }

}