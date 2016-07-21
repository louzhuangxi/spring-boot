package com.base.spring.config;

import com.base.spring.filter.XSSFilter;
import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// web 配置
//WebMvcConfigurerAdapter 中有更多设置，参考文档
@Configuration
class WebMVCConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebMVCConfig.class);

    // 向系统注册一个 RequestContextListener Bean ，这样在其他组件中就可以使用了
    //  CustomUserDetailsService 用到，用于截获 HttpServletRequest
    //  @Autowired
    //  private HttpServletRequest request;
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }


    // 自定义过滤器和监听器
    // http://blog.jobbole.com/97760/
    //http://blog.jobbole.com/97763/
    // 所有过滤器的调用顺序跟添加的顺序相反，过滤器的实现是责任链模式，

    /**
     * 自定义  listener 演示
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
     * 将代理服务器发来的请求包含的IP地址转换成真正的用户IP
     *
     * @return
     */
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        logger.info("RemoteIpFilter initialized");
        return new RemoteIpFilter();
    }

    /**
     * 自定义 filter，增加 XSSFilter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XSSFilter());
        registration.addUrlPatterns("/*");
        registration.setName("XSSFilter");
        logger.info("XSSFilter initialized");
        return registration;
    }


    /**
     * 注册一个拦截器
     * 拦截器只在 spring web 中使用
     * filter 可以在 java web 中使用，范围广
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor()).addPathPatterns("/person/save/*");
//    }

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