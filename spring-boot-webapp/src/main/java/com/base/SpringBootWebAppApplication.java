package com.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// 注册  http://www.baeldung.com/spring-security-registration?utm_source=email&utm_medium=email&utm_content=lss_registration&utm_campaign=campaign_lss

//http://www.baeldung.com/spring-security-expressions?utm_source=email&utm_medium=email&utm_content=lss_expressions&utm_campaign=campaign_lss
 //http://www.baeldung.com/role-and-privilege-for-spring-security-registration?utm_source=email&utm_medium=email&utm_content=lss_roles_permissions&utm_campaign=campaign_lss&tl_inbound=1&tl_target_all=1&tl_period_type=3

//http://kielczewski.eu/2014/12/spring-boot-security-application/

//https://git.oschina.net/jeff1993/springboot-learning-example

// 邮箱激活 http://www.baeldung.com/registration-verify-user-by-email?utm_source=email-newsletter&utm_medium=email&utm_campaign=auto_47_sec
// 增加反爬虫机制


//权限管理例子
//1 https://www.oschina.net/p/uniauth-
//https://github.com/wayshall/onetwo
@SpringBootApplication  // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootWebAppApplication extends SpringBootServletInitializer {

    ??
    logback      RollingFileAppender 没有测试

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebAppApplication.class, args);
    }

    /**
     * 生成 war 步骤 （参见官方文档）
     * 1. 继承 extends SpringBootServletInitializer ，使之支持 servlet
     * 2. 修改 pom  <packaging>war</packaging>
     * 3. 引入 spring-boot-starter-jetty 或 tomcat，为项目运行服务器, 依赖包不发布 <scope>provided</scope>
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebAppApplication.class);
    }

}
