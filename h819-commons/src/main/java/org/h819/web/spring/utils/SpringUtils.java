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
     * 在非 Spring bean （无 @Component 注释的类，无法使用 @Autowired 自动注入 ）中 + web 环境下，获取 bean
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