package com.base.spring.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/22
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Component
@PropertySource(value = "classpath:ftp.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "ftp")
@Data
//使用见 FtpPropertiesTest
public class FtpProperty {

    //map example
    private final Map<String, String> infos = new HashMap<>();

    @NotEmpty
    private String url;
    private int port;
    @NotEmpty
    private List<User> users = new ArrayList<>();

    @Data
    public static class User {
        private String name;
        private String password;
    }

    /**
     * 使用
     * 在任意 bean 中
     * @Autowired private FtpProperties ftpProperties;
     */
}
