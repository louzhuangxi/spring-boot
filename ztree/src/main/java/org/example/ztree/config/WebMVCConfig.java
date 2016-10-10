package org.example.ztree.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// web 配置
@Configuration
class WebMVCConfig extends WebMvcConfigurerAdapter {

    /**
     * 自定义  listener 演示
     * 此处用作初始化相关的数据
     *
     * @return
     */
    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {

                System.out.println("ServletContext destroyed");
            }

        };
    }

    /**
     * Spring 3.2 及以上版本自动开启检测URL后缀,设置Response content-type功能, 如果不手动关闭这个功能,当url后缀与accept头不一致时,
     * Response的content-type将会和request的accept不一致,导致报 406 错误
     * 例如返回类型为 JSON 的 @ResponseBody API, 必须将请求URL后缀改为.json，以便和 accept头(application/json)相匹配，否则返回 406 错误。
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        configurer.favorPathExtension(false). //关闭URL后缀检测的方法如下
                favorParameter(true).
                mediaType("json", MediaType.APPLICATION_JSON).
                defaultContentType(MediaType.APPLICATION_JSON);//如果没有对应的后缀名，返回信息默认以 json 格式返回

    }

    /**
     * PathMatchConfigurer 函数让开发人员可以根据需求定制URL路径的匹配规则。
     *
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /**
         * spring mvc 默认忽略 url 中点"."后面的部分，如
         * http://localhost:8080/abc.mm  会直接匹配为
         * http://localhost:8080/abc 忽略了 mm
         * 如果不想忽略，设置 setUseSuffixPatternMatch(false)
         */

        configurer.setUseSuffixPatternMatch(false);
    }

}