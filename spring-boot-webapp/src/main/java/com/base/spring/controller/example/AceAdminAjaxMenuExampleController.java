package com.base.spring.controller.example;

import org.h819.web.commons.MyServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * <p/>
 * Description : TODO(ace admin template ajax 方式导航)
 * assets/js/ace/ace.js 文件中，控制 ajax 方式点击之后，自动转换为的 url 的写法
 * 我做了改造，使之
 * 1.
 * - var path="/base/menu/ajax/index.html";
 * - var hash = "page/dashboard";
 * - 转换为   -> /base/menu/ajax/content/dashboard.html
 * 2.
 * var path="/base/menu/ajax/index.html";
 * var hash = "page/dashboard?abc=1&bcd=2";
 * - 转换为   -> /base/menu/ajax/content/dashboard.html?abc=1&bcd=2
 * ---
 */
@Controller
@RequestMapping("/ace/example/ajax") //重要: 必须以 ajax 结尾，以符合 ace.js 中 content_url 的要求，ace/example 为前缀，可以为任意值或者没有，解释如上
public class AceAdminAjaxMenuExampleController {

    private static Logger logger = LoggerFactory.getLogger(AceAdminAjaxMenuExampleController.class);


    /**
     * ajax url :  http://localhost:8888/base/ace/example/ajax/index.html#page/index
     * 被解析为  :  http://localhost:8888/base/ace/example/ajax/index.html (注意没有 content)
     * 跳转到真正的页面 :  html/ajax/content/menu.ftl
     *
     * @return
     */
    @RequestMapping(value = "index.html", method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        logger.info("request path={} ,  will go to /html/ajax/index.ftl", MyServletUtils.getFullPath(request));
        return "admin/ace/html/ajax/index";
    }


    /**
     * ajax url :  http://localhost:8888/base/ace/example/ajax/index.html#page/menu
     * 被解析为  :  http://localhost:8888/base/ace/example/ajax/content/menu.html
     * 跳转到真正的页面 :  html/ajax/content/menu.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/menu.html")  //必须有 /content/
    public String menu(HttpServletRequest request) {

        logger.info("request path={} ,  will go to /html/ajax/content/menu.ftl", MyServletUtils.getFullPath(request));
        return "admin/ace/html/ajax/content/menu";
    }

    // ...... 其他页面的例子

}