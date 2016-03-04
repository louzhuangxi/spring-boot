package com.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;


/**
 * http client 方式，尝试和很久，有很多未解问题，不再尝试，统一用 spring oauth 版本
 * 另外 apache oltu 也有 oauth server 和 client
 * <p/>
 * 必需运行在 web 下
 * <p/>
 * 测试时，加上
 *
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @SpringApplicationConfiguration(classes = SpringOauth2ClientApplication.class)
 * @WebAppConfiguration -
 * 即可运行在web环境下
 */

@SpringBootApplication
public class SpringOauth2ClientApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringOauth2ClientApplication.class, args);
    }


}