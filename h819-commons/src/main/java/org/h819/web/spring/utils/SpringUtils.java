package org.h819.web.spring.utils;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Jianghui
 * Date: 16-7-2
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */

public class SpringUtils {

    /**
     * 其他几个工具类
     */
    //org.springframework.web.util.WebUtils
    //org.springframework.web.util.HtmlUtils

    /**
     * 普通类中获取被Spring托管的Bean ，这种方式比较适合于那些B/S架构的web应用
     * 在 Servlet 容器（如 tomcat 服务器）中，spring mvc 环境下，在非 Spring 类 （无 @Component 注释的类，无法使用 @Autowired 自动注入 ）中 ，获取 bean
     * 如在 普通普通的 servelet 中获取
     * -
     * //@Autowired
     * // UserService service;  // 编译错误，服务注入
     *
     * @param request
     * @param beanName
     * @return
     */
    public static <T> T getWebApplicationContextBean(HttpServletRequest request, String beanName) {


        //使用：UserService service = SpringUtils.getWebApplicationContextBean(httpServletRequest,"userService");
        return (T) WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(beanName);
    }


}