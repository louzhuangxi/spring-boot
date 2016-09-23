package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//http://kielczewski.eu/2014/12/spring-boot-security-application/

// 邮箱激活 http://www.baeldung.com/registration-verify-user-by-email?utm_source=email-newsletter&utm_medium=email&utm_campaign=auto_47_sec

@SpringBootApplication  // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableJpaAuditing // 开启 auditing ，使用方法见 AbstractMySQLEntity
public class SpringBootWebAppApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebAppApplication.class, args);
    }

    /**
     * 生成 war 步骤 （参见官方文档）
     * 1. 继承 extends SpringBootServletInitializer ，使之支持 servlet
     * 2. 修改 pom  <packaging>war</packaging>
     * 3. 引入 spring-boot-starter-jetty 或 tomcat，为项目运行服务器, <scope>provided</scope>
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebAppApplication.class);
    }

}
