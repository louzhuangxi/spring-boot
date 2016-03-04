package org.baeldung.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableWebMvc
//@ComponentScan({"org.baeldung.web.controller"})
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true) //启用  @PreAuthorize 注释安全验证 / 启用级联加载对象特性(proxyTargetClass = true)
//First thing to mention is @Order annotation, which basically keeps all the defaults set by Spring Boot, only overriding them in this file.
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity // 启用 web 认证和授权
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 访问控制，那些资源路径，哪些角色可以访问
     * 和 spring security 配置方法相同
     *
     *
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        /**
         *  oauth client 验证
         *
         *  可以统一写在这里，也可以标注在 controller 的方法上
         *  @PreAuthorize("#oauth2.hasScope('read')")
         *
         *  --
         *
         *  1. #oauth2.hasRole('USER')
         *  -  Check if the OAuth2 client (not the user) has the role specified. To check the user's roles see #hasRole(String).
         *  -
         *  2. #hasAnyRole('USER','ADMIN')
         *  - Check if the OAuth2 client (not the user) has one of the roles specified. To check the user's roles see #hasAnyRole(String).
         *  -
         *  3. #oauth2.hasScope('read')
         *  -Check if the current OAuth2 authentication has one of the scopes specified.
         *  -
         *  4. #oauth2.hasAnyScope('read','write')
         *  - Check if the current OAuth2 authentication has one of the scopes specified.
         */

        /**
         * 经过测试，在此处配置，不起作用，只能在 controller 上用 PreAuthorize ，不知道
         */
//
//        http
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .authorizeRequests()
//                        // User resource
//                .antMatchers(HttpMethod.GET, CLIENT_URL).hasAnyAuthority("root", "client")
//                .antMatchers(HttpMethod.POST, CLIENT_URL).hasAnyAuthority("root", "client")
//                .antMatchers(HttpMethod.PUT, CLIENT_URL).hasAnyAuthority("root", "client")
//                .antMatchers(HttpMethod.DELETE, CLIENT_URL).hasAnyAuthority("root", "client")
//
//                // User resource
//                .antMatchers(HttpMethod.GET, USER_URL).hasAnyAuthority("root", "user")
//                .antMatchers(HttpMethod.POST, USER_URL).hasAnyAuthority("root", "user")
//                .antMatchers(HttpMethod.PUT, USER_URL).hasAnyAuthority("root", "user")
//                .antMatchers(HttpMethod.DELETE, USER_URL).hasAnyAuthority("root", "user")
//
//                // role resource
//                .antMatchers(HttpMethod.GET, ROLE_URL).hasAnyAuthority("root", "role")
//                .antMatchers(HttpMethod.POST, ROLE_URL).hasAnyAuthority("root", "role")
//                .antMatchers(HttpMethod.PUT, ROLE_URL).hasAnyAuthority("root", "role")
//                .antMatchers(HttpMethod.DELETE, ROLE_URL).hasAnyAuthority("root", "role")
//
//                // group resource
//                .antMatchers(HttpMethod.GET, GROUP_URL).hasAnyAuthority("root", "group")
//                .antMatchers(HttpMethod.POST, GROUP_URL).hasAnyAuthority("root", "group")
//                .antMatchers(HttpMethod.PUT, GROUP_URL).hasAnyAuthority("root", "group")
//                .antMatchers(HttpMethod.DELETE, GROUP_URL).hasAnyAuthority("root", "group")
//
//                // resource resource
//                .antMatchers(HttpMethod.GET, RESOURCE_URL).hasAnyAuthority("root", "resource")
//                .antMatchers(HttpMethod.POST, RESOURCE_URL).hasAnyAuthority("root", "resource")
//                .antMatchers(HttpMethod.PUT, RESOURCE_URL).hasAnyAuthority("root", "resource")
//                .antMatchers(HttpMethod.DELETE, RESOURCE_URL).hasAnyAuthority("root", "resource")


                /**
                 * oauth2 的 client 端验证
                 */
             //  http.authorizeRequests().antMatchers(HttpMethod.GET, "/foos/**").access("#oauth2.hasScope('write')");
    }

}
