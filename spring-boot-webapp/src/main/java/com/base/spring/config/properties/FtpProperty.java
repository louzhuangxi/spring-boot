package com.base.spring.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/22
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Component
@ConfigurationProperties(prefix = "ftp")
@Getter
@Setter
public class FtpProperty {


    //可以进行验证 ，未进一步研究
    // org.hibernate.validator.constraints
    //javax.validation
    @NotEmpty
    private String url;
    private int port;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    /**
     * 使用
     * 在任意 bean 中
     * @Autowired private FtpProperties ftpProperties;
     */

}
