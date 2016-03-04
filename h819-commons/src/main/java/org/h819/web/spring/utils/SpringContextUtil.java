package org.h819.web.spring.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 该类用来获取 Spring ApplicationContext，从而在普通类或 jsp 中可以直接获取spring 管理的 Bean ，进而调用 Bean 的方法
 * 代码例子：
 * HelloDBController h = (HelloDBController) SpringContextUtil.getBean("helloDBController");
 * 调用之后，常驻内存，可以随时使用，而不必每次进行查询操作
 * User: Jianghui
 * Date: 13-12-2
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */

@Component
@Lazy(false) // spring 默认 lazy-init="true" ,无法直接使用，修改为 false
 public class SpringContextUtil implements ApplicationContextAware {

   // private static SpringContextUtil context = new SpringContextUtil();

    private static ApplicationContext ctx;

    public static ApplicationContext getApplicationInstance() {
        return SpringContextUtil.ctx;
    }

    public static Object getBean(String beanName) {
        return SpringContextUtil.ctx.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return SpringContextUtil.ctx.getBean(beanName, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {

            SpringContextUtil.ctx = ctx;
        //  ctx.

    }
}