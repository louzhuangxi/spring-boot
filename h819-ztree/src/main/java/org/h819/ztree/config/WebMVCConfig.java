package org.h819.ztree.config;

import org.h819.ztree.service.InitializeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// web 配置
@Configuration
class WebMVCConfig extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebMVCConfig.class);


    @Autowired
    InitializeService initializeService;

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
                logger.info("ServletContext destroyed");
            }

        };
    }

    /**
     * 自定义 filter 演示
     *
     * @return

     @Bean public FilterRegistrationBean someFilterRegistration() {
     FilterRegistrationBean registration = new FilterRegistrationBean();
     registration.setFilter(new CustomFilter());
     registration.addUrlPatterns("/*");
     // registration.addInitParameter("paramName", "paramValue");
     registration.setName("someFilter");
     return registration;
     }
     */

    /**
     * Spring 3.2 及以上版本自动开启检测URL后缀,设置Response content-type功能, 如果不手动关闭这个功能,当url后缀与accept头不一致时,
     * Response的content-type将会和request的accept不一致,导致报 406 错误
     * 例如返回类型为 JSON 的 @ResponseBody API, 必须将请求URL后缀改为.json，以便和 accept头(application/json)相匹配，否则返回 406 错误。
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        configurer.favorPathExtension(false). //关闭URL后缀检测的方法如下
                favorParameter(true).
                parameterName("mediaType").
                ignoreAcceptHeader(true).
                useJaf(false).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON).
                defaultContentType(MediaType.APPLICATION_JSON);//如果没有对应的后缀名，返回信息默认以 json 格式返回

    }

}