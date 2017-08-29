package com.base.spring.controller.example.tree;

import lombok.extern.slf4j.Slf4j;
import org.h819.web.commons.MyServletUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 树管理页面
 */

@Controller
@RequestMapping("/example/tree/manage") //重要: 必须以 ajax 结尾，以符合 ace content_url 的要求
@Slf4j
public class TreeExampleController {

    //private static final log log = LoggerFactory.getLogger(TreeExampleController.class);

    @RequestMapping(value = "/ztree.html", method = RequestMethod.GET)
    public String ztree(HttpServletRequest request, Model model) {
        log.info("go to ztree");
        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("menu_type", "Menu");
        return "admin/ace/html/ajax/ztree-example";
    }

    @RequestMapping(value = "/fuelux.html", method = RequestMethod.GET)
    public String fuelux() {
        log.info("go to fueluxtree");
        return "admin/ace/html/ajax/fueluxtree-example";
    }

}