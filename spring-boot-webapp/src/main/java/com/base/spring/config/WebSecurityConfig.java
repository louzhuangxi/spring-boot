package com.base.spring.config;

import com.base.spring.service.security.CustomUserDetailsService;
import com.base.spring.utils.BCryptPassWordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//4.1 新特性
//http://docs.spring.io/spring-security/site/docs/4.1.x/reference/htmlsingle/#new

// 经过 spring security 处理后，controller 就不需要再理会“登陆”或者“退出”等权限验证问题，这些问题都交给 spring security 了。

// spring security 中，如果想要自动加载 fetch = FetchType.LAZY 的级联对象，必需设置
//1. @EnableGlobalMethodSecurity(proxyTargetClass = true)
//2. @EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY) //此项也可以保证，在非 spring security 中也可以加载级联对象
//3. 用到级联对象的方法上要有 @Transactional 标记
// ( google 了一整天，没有明确答案，自己试验出来的)

//example :
// http://www.javacodegeeks.com/2015/10/spring-boot-oauth2-security.html   spring oauth
//http://spring.io/blog/2015/10/28/react-js-and-spring-data-rest-part-5-security
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
//启用  @PreAuthorize 注释安全验证 / 启用级联加载对象特性(proxyTargetClass = true)
//First thing to mention is @Order annotation, which basically keeps all the defaults set by Spring Boot, only overriding them in this file.
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity // 启用 web 认证和授权
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier(value = "customAuthenticationFailureHandler")//本类中定义的 Component
//    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * URL-based security set up
     * url 方式进行授权(spring security 权限模式的一种)，简化了在每个操作之前都需要的权限判断，只在满足 url 时才进行判断，是一种简单的权限模式。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // spring security 自己维护着一个 Context (SecurityContextHolder Context) ，并和 spring context 适时进行交互
        // 每个 url 请求，均由 Spring Security 进行接管验证。
        // 认证授权过程：
        // 1. 访问 url

        // 2. 查看 SecurityContextHolder Context 中 UserDetails 是否存在，并且是否通过验证

        // 2.1 如果用户不存在，则强制跳转到 login 页面进行登陆(/login 请求，需为 RequestMethod.POST)
        // 2.1.1   由 WebSecurityConfig.configure(AuthenticationManagerBuilder auth) 方法接管，查找用户，(UserDetailsService.loadUserByUsername())返回找到的用户(如果找不到，返回用户名 anonymousUser 的用户默认用户)，并存入 SecurityContextHolder Context
        // 2.1.2   比较登陆时传递过来的 password 和 SecurityContextHolder Context 中的 UserDetails 的 password 是否相同。相同则通过验证，不同则强制跳转到登陆页。

        // 2.2 如果用户存在且通过验证，可以直接访问系统，不再进行验证

        // Spring Security 中 hasAuthority,hasRole 区别:
        // hasAuthority("ADMIN") 方法: 角色名称就不能添加 ROLE_ 前缀，只能为 ADMIN ,USER
        // hasRole("ROLE_ADMIN") 方法: 则需要有前缀, ROLE_ADMIN , ROLE_USER

//        The login form is under /login, permitted for all. On login failure a redirect to /login?error will happen. We have LoginController mapping this URL.
//        The parameter holding the username in login form is called 'email' because this is what we're using as username.
//        The logout URL is /logout, permitted for all. After that, the user will be redirected to /. One important remark here is that if the CSRF protection is turned on, the request to /logout should be POST.


        // .permitAll() 和 .anonymous() 前者是不需要任何验证，后者是 anonymous 角色
        //   .permitAll() 和  security.ignoring() 区别：
        //   前者登陆和不登陆用户都可以访问，并且在路径下可以获得当前已登录用户的principal(不登陆页面为 anonymousUser),后者不经过 spring security 处理，直接忽略
        //  HttpSecurity antMatchers 没有提到的 url ，都会被忽略，相当于 WebSecurity ignoring()


        // remember-me login page 中的表单名字
        /**
         *  当超过最大的 maximumSessions 的数目时
         */
        // 1. 会返回默认的错误提示 "This session has been expired (possibly due to multiple concurrent logins being attempted as the same user)."
        // 2. 可以自定义个页面
        http.authorizeRequests()
                .antMatchers("/", "/signup", "/about", "/policies").permitAll() // Allow anyone (including unauthenticated users) to access to the URLs 登陆和登陆用户都可以访问
                .antMatchers("/admin/**", "/users/**").hasAuthority("ADMIN") //  .hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated() //所有资源，均需要过认证，不需要权限  All remaining URLs require that the user be successfully authenticated
                .and().formLogin()

                // 只接受 /login POST 方法的请求，并处理 ,/login GET 方法会被忽略.请求处理见下面 configure(AuthenticationManagerBuilder auth)  的配置
                // loginPage : when authentication is required, redirect the browser to /login , it is GET request  : 跳转到登陆页面的 url ，只接受 GET 请求，这个 url 仅起跳转到登陆页面的作用
                // loginProcessingUrl : 登陆页面的 from action url ，只接受 POST 请求。在 等于页面设置好即可，不必在 Controller 中设置，spring security 验证机制自动和 loginProcessingUrl 匹配，并进行验证。
                // 如果 loginProcessingUrl 不写，默认和 loginPage 相同
                .loginPage("/login").loginProcessingUrl("/login_process").defaultSuccessUrl("/ajax/index.html").failureUrl("/login?error=abcdedf").usernameParameter("login_email").passwordParameter("login_password").permitAll()
                .and().rememberMe()

                .rememberMeParameter("remember-me") // remember me 不安全，其 cookies 容易被劫持。有更安全的方案，待查。
                .and().logout()
                .logoutSuccessUrl("login?logout").logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/").permitAll()
                .and().sessionManagement()//.sessionFixation().changeSessionId() changeSessionId 避免固定会话攻击
                .invalidSessionUrl("/login?session=invalid") //
                .maximumSessions(2).maxSessionsPreventsLogin(true) //限制用户只能从一个设备登陆，不同同时登陆多台设备
        ; //

        http.headers().xssProtection(); //
        http.csrf();
    }


    /**
     * 不经过 spring security 处理，直接忽略，可以提升系统性能
     */
    @Override

    public void configure(WebSecurity security) {
        // All of Spring Security will ignore the requests

        /**
         * "/**"  : 测试是此开关打开
         */
        security.ignoring().antMatchers("/**"); //测试时，不加安全验证
        //
        security.ignoring()
                .antMatchers("/resource/**", "/public/**", "/static/**", "/css/**", "/js/**", "/img/**", "**/favicon.ico");

        /**
         *
         */
        //css,js 等文件放在 resources/templates/static/ace 目录下，访问时 url 是 http://localhost/ace/ace.css
        security.ignoring().antMatchers("/ace/**");
        //登陆、注册、找回密码的验证的 ajax ，需要不登陆就能访问
        security.ignoring().antMatchers("/validate/ajax/**");
    }

    /**
     * /login POST 类型的 submit , 根据 login 页面传递过来的参数，进行如下处理
     * auth.userDetailsService() 方法的参数为 org.springframework.security.core.userdetails.UserDetailsService 类型。
     * CustomUserDetailsService 扩展了该类型
     * 注意：保存用户信息的时候，密码也要用 BCryptPasswordEncoder 加密。
     * <p>
     * userDetailsService 只负责验证用户名是否存在
     * 本方法再加上一个密码验证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        //LDAP,OpenId 等其他验证，返回其他他值即可
        //密码加密方式为 BCrypt
        auth.userDetailsService(userDetailsService).passwordEncoder(BCryptPassWordUtils.getBCryptPasswordEncoder());
    }

}