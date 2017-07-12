package org.examples.spring.controller.aceadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 期间遇到了很多访问的麻烦，无法解决。下一版，利用 SiteMesh(或其他 jquery 有个 iframe) 修改，不用下面的东西
 *
 *
 * <p/>
 * Description :
 * 处理 ace admin 模版，ajax 方式菜单跳转
 * 由于 ajax 方式的菜单跳转是通过 js 方式处理的，是静态跳转。
 * 而 spring mvc 处理 url 是通过 controller 方式，路径并不是真正的页面路径，其路径不能符合 ajax 方式的 js 函数静态路径要求
 * 本函数，根据 ajax.html 页面中， js 方法中路径跳转规则，设置了 controller 参数
 * 可在不修改 ajax.html 中的 js 函数的情况下，在各个页面中自由跳转。
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */

/**
 * 步骤：
 * 0. 静态资源不需要通过 spring ，把 ace admin 的静态资源（css,js,图片等）放到 web 根目录下，如放到 /static/ace/ 下，jsp 页面放到 WEB-INF 下如 WEB-INF/jsp/下
 * 1. 把 ajax.html 变为 ajax.jsp 文件(文件名字任意)，以便于 spring mvc 跳转调用
 * 2. 其他页面，应和 ajax.jsp 同目录，否则默认的 js 函数调整会有问题(约定:都放在 ace/html/ajax 目录下)
 * 3. 每个 jsp 页面加入如下代码
 *  <%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
 *  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 *  <c:set var="ctx" value="${pageContext.request.contextPath}"/>
 *  4.把 每个 jsp 页面的静态资源，通过 ${ctx}/static/ace/assets/ 调用
 *  5. 页面访问 /page/ajax.html#page/login 即可带框架，访问 webapp/WEB-INF/jsp/ace/html/ajax/login.jsp (即带上 ajax.html 这个头, # 后面是 controller 指定的 jsp 页面)
 *  7.页面的 header , footer , sidebar 均在 ajax.jsp 中修改，其他页面只关注自己的内容即可
 *  8.升级时，可以比较 ace 源代码变化即可
 * <p/>
 * <p/>
 */
@Controller
@RequestMapping("/page") //不要更改，以符合ajax.html 中 js 函数要求
public class AceAdminAjaxMenuController {

    private static final Logger logger = LoggerFactory.getLogger(AceAdminAjaxMenuController.class);

    /**
     * 访问 ajax.jsp
     *
     * @return
     */
    @RequestMapping(value = "/ajax.html")
    public String ajax(HttpServletRequest request) {

        //默认一个登陆用户，日后修改为登陆验证后，修改此处

        if (request.getSession().getAttribute("user_id") == null)
            request.getSession().setAttribute("user_id", "3");
        return "ace/html/ajax/ajax";
    }

    /**
     * 访问 index.jsp
     *
     * @return
     */
    @RequestMapping(value = "/index.html")
    public String index() {
        return "ace/html/ajax/index";
    }

    /**
     * 访问 login.jsp
     * <p/>
     * url 为 http://localhost:8888/info/page/login.html
     * 不通过 ajax 方式( http://localhost:8888/info/page/ajax.html#page/login )
     * 以便于不显示 sidebar 菜单
     * login.jsp 用 html/login.html 改编，不用 ajax/login.html ，以便在 login.html 文件中引入 css 和 js
     *
     * @return
     */
    @RequestMapping(value = "/login.html")
    public String login() {
        return "ace/html/ajax/login";
    }

}