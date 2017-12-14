package com.base.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/11/17
 * Time: 9:36
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableJpaAuditing // 开启 auditing ，使用方法见 AbstractMySQLEntity
// jpa 2.1 不支持 java 8 LocalDate ，注册 Jsr310JpaConverters ，是 jpa 能够正确保存 jdk8 日期，否则存储到数据库后，日期是 bolb 类型
//@EntityScan(basePackageClasses = {SpringBootWebAppApplication.class, Jsr310JpaConverters.class})
public class JpaConfig implements AuditorAware<String> {


    /**
     * 自定义 Audit CreatedBy / LastModifiedBy用到的用户名
     *
     * @return
     */
    @Override
    public String getCurrentAuditor() {

        //此处自定义获取用户名的方法
        //spring security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((User) authentication.getPrincipal()).getUsername();

        //或者在非 spring security 下，  返回当前session会话中的账户名
    }
}
