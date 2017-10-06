package com.example.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.example.mybatis.mapper")
public class SpringBootMybatisApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisApplication.class, args);
    }

    /**
     * 生成 war 步骤 （参见官方文档）
     * 1. 继承 extends SpringBootServletInitializer ，使之支持 servlet
     * 2. 修改 pom  <packaging>war</packaging>
     * 3. 引入 spring-boot-starter-jetty 或 tomcat，为项目运行服务器, 依赖包不发布 <scope>provided</scope>
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootMybatisApplication.class);
    }

}
