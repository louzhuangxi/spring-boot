package com.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

//http://kielczewski.eu/2014/12/spring-boot-security-application/

// 邮箱激活 http://www.baeldung.com/registration-verify-user-by-email?utm_source=email-newsletter&utm_medium=email&utm_campaign=auto_47_sec

@SpringBootApplication  // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootWebappApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebappApplication.class, args);
    }

    /**
     * 生成 war 步骤 （参见官方文档）
     * 1. 继承 extends SpringBootServletInitializer
     * 2. 修改 pom  <packaging>war</packaging>
     * 3. spring-boot-starter-web ，打包的时候排除 tomcat
     * 4. 引入 spring-boot-starter-jetty 或 tomcat，为项目运行服务器, <scope>provided</scope>
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebappApplication.class);
    }
}
