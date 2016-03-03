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
 * 需要注意的是，必须有 content 而且 不能改为其他的，不知道为什么(是不是和文件夹相关？)，就严格按照下面的写法吧，尝试了很多，都不行
 */
// ace admin 1.3.4 -> assets/js/ace/ace.js 文件改造后
//var demo_ajax_options = {
//        'close_active' : true,
//
//        'default_url' : 'page/index', //default hash
//        'content_url' : function (hash) {
//        //***NOTE***
//        //this is for Ace demo only, you should change it to return a valid URL
//        //please refer to documentation for more info
//
//        if (!hash.match(/^page\//))
//        return false;
//        var path = document.location.pathname;
//        console.log("path=" + path);
//        console.log("hash=" + hash);
//
//        //for example in Ace HTML demo version we convert /ajax/index.html#page/gallery to > /ajax/content/gallery.html and load it
//        //if(path.match(/(\/ajax\/)(index\.html)?/)){
//        if (path.indexOf("/ajax/index.html") > 0) {
//
//        var temp = path.replace(/(\/ajax\/)(index\.html)/, '/ajax/content/'+hash.replace(/^page\//, '')+'.html') ;
//        console.log("temp=" + temp);
//        if (hash.indexOf("?") > 0) {
//        var pagename = hash.substring(hash.lastIndexOf("/")+1,hash.lastIndexOf("?"));
//        temp = path.replace(/(\/ajax\/)(index\.html)(.*)/, '/ajax/content/'+pagename+'.html') ;
//        var hash_para_name = "?" + hash.replace(/.*\?/, '');
//        temp= temp+hash_para_name;
//        console.log("hash_para_name=" + hash_para_name);
//
//        }
//
//        console.log("temp2=" + temp);
//        return temp;
//
//        }
//        //for example in Ace PHP demo version we convert "ajax.php#page/dashboard" to "ajax.php?page=dashboard" and load it
//        console.log(path + "?" + hash.replace(/\//, "="));
//        return path + "?" + hash.replace(/\//, "=");
//        }
//        }
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