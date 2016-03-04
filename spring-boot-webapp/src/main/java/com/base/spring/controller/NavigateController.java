package com.base.spring.controller;

import org.h819.web.commons.MyServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : TODO(ace admin template ajax 方式导航, 本项目用到。都是点击菜单传来，所以都是 Get)
 * 参见 AceAdminAjaxMenuExampleController 说明
 */

@Controller
@RequestMapping("/menu/ajax")
//重要: 必须以 ajax 结尾，以符合 ace.js 中 content_url 的要求，menu 为前缀，可以为任意值或者没有，解释见 AceAdminAjaxMenuExampleController
public class NavigateController {

    private static Logger logger = LoggerFactory.getLogger(NavigateController.class);


    /**
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/index
     * 被解析为  :  http://localhost:8888/base/menu/ajax/index.html (注意没有 content)
     * 跳转到真正的页面 :  html/ajax/index.ftl
     * index.ftl 文件仅是一个导航文件，没有内容
     * index.ftl 会指向 ajax 方法，去加载 content/index.html 文件，该文件不存在，显示空白
     *
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
//        logger.info("getPathInfo={} , getPathTranslated ={} ,getRequestURI={} , getRequestURL={} , getLocalAddr={} ,getRemoteAddr ={},getQueryString={}",
//                request.getPathInfo(), request.getPathTranslated(), request.getRequestURI(), MyServletUtils.getFullPath(request)
//                , request.getLocalAddr(), request.getRemoteAddr(),request.getQueryString());
        logger.info("request path={} ,  will go to /html/ajax/index.ftl", MyServletUtils.getFullPath(request));
        return "admin/ace/html/ajax/index";
    }


    /**
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/menu
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/menu.html
     * 跳转到真正的页面 :  html/ajax/content/menu.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/jqgrid-menu.html", method = RequestMethod.GET)  // 必须有 /content/
    public String menu(HttpServletRequest request) {

        logger.info("request path={} ,  will go to /html/ajax/content/menu.ftl", MyServletUtils.getFullPath(request));
        return "admin/ace/html/ajax/content/menu";
    }

    /**
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/ztree
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/ztree.html
     * 跳转到真正的页面 :  html/ajax/content/menu-tree.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/ztree.html", method = RequestMethod.GET)    // 必须有 /content/
    public String menutree(@RequestParam(value = "type",required = true) String type, HttpServletRequest request, Model model) {
        logger.info("request path={} , type={},  will go to /html/ajax/content/ztree.ftl", MyServletUtils.getFullPath(request), type);

        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("menu_type", type);
        return "admin/ace/html/ajax/content/ztree";
    }


    /**
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/fuelux-tree
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/fuelux-tree.html
     * 跳转到真正的页面 :  html/ajax/content/fuelux-tree.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/fuelux-tree.html", method = RequestMethod.GET)    // 必须有 /content/
    public String fueluxTree(@RequestParam(value = "type",required = true) String type, HttpServletRequest request, Model model) {
        logger.info("request path={} , type={},  will go to /html/ajax/content/fuelux-tree.ftl", MyServletUtils.getFullPath(request), type);

        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("menu_type", type);
        return "admin/ace/html/ajax/content/fuelux-tree";
    }

    // ...... 其他页面的例子

}