package com.base.spring.config;

import com.base.spring.custom.security.CustomAuthenticationFailureHandler;
import com.base.spring.custom.security.CustomAuthenticationSuccessHandler;
import com.base.spring.service.security.CustomUserDetailsService;
import com.base.spring.utils.BCryptPassWordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// apache shiro
//https://developer.okta.com/blog/2017/07/13/apache-shiro-spring-boot

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
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) //spring boot 2 去掉，不知道行不行
@EnableWebSecurity // 启用 web 认证和授权
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier(value = "customAuthenticationFailureHandler")//本类中定义的 Component
//    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    CustomAuthenticationFailureHandler failureHandler;
    @Autowired
    CustomAuthenticationSuccessHandler successHandler;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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

        /**
         * spring security url 设计原则
         *
         * ===== ajax 请求的处理 ====
         * -
         * ajax 请求，不需要点击，浏览器自动异步执行，所以不能通过控制是否在页面显示菜单或者按钮，来控制用户是否点击执行。
         * 恶意用户假如知道了该 ajax 请求地址之后，会直接发送该 ajax 请求
         * -
         * 如何通过 url 来控制 ajax 请求？
         * （ajax 的 url 参数，只能设置一个 url 地址）
         * 假设系统中，有一个页面通过列表展示所有文章，点击菜单导航到该展示页面之后，该页面会发送 ajax 请求，返回所有文章后显示在页面上
         * -
         * 1. 这个列表内容，如果开放给系统管理员、注册用户和不需要验证的互联网的所有用户，此时该 ajax 请求不需要控制。
         *    (方法 ：   security.ignoring().antMatchers("/ajax/tree/ztree/asyncByTreeType.html");)
         * -
         * 2. 如果不能开放给不需要验证的用户，只能开放给系统管理员和注册用户，此时该 ajax 需要控制只给系统管理员和注册用户
         *    (因为只能有一个 url 参数，所以无法在 controller 的 访问路径上用两个 url 来代表两种身份，如 @RequestMapping(value = {"/admin","user"})
         *    只能是开发给注册用户  antMatchers("/ajax/**").authenticated()
         *
         * -
         * 3. 如果只能开放给系统管理员和注册用户，但系统管理员和注册用户看到的内容还有区别，怎么办？有两种方式
         * -
         * 3.1 建立两个页面 list-admin.flt , list-user.flt ，两个页面只是 ajax 请求路径不一样，如
         *     list-admin.flt 中 ajax 路径是 /ajax/admin/list/** ，通过路径控制，只给 admin 用户访问，
         *     list-user.flt 中 ajax 路径是 /ajax/user/list/** ，通过路径控制，只给 user 用户访问
         *     其他的页面代码内容完全一致。
         *     这种方式不好，一样的页面有两个文件，冗余，可维护性不好
         * 3.2 开发一个页面 list.flt ，ajax 访问路径就是 /ajax/list/** ，此路径开放给系统管理员和注册用户
         *     在处理 /ajax/list/** 请求的方法中，区分 admin , user ，从而返回不同的结果。
         *     .antMatchers("/ajax/**").authenticated()
         * -
         * -
         * ===== 其他请求的处理 ====
         *
         * 1 只开放给 admin : 只有 admin 使用
         *   1)  .antMatchers("/admin/**").hasAuthority("ADMIN")
         *   2.  @RequestMapping(value = {"/admin"})
         * -
         * 2 只开放给 user  : 只有 user 使用
         *   1)  .antMatchers("/user/**").hasAuthority("USER")
         *   2.  @RequestMapping(value = {"/user"})
         * -
         * 3 开放给 admin 和 user ： 通用的访问数据的方法，admin/user 操作过程一样，只是根据身份不同返回的数据不同
         *   1)  .antMatchers("/admin/**","/user/**").authenticated()
         *   2)  @RequestMapping(value = {"/admin","user"})
         *   3)  如果需要区分身份，在处理的方法中进行区分
         *
         * ===== 总结 ====
         *
         * 1. url 设计
         * -
         *  1) 所有的 ajax 请求，controller 路径统一为 /ajax/... 开始，便于用 url 控制
         *  2) 只开放给 admin , ，controller 路径统一为 /admin/... 开始，便于用 url 控制
         *  3) 只开放给 user , ，controller 路径统一为 /user/... 开始，便于用 url 控制
         *  4) 只开放给 admin 和 user ，controller 路径统一为 /admin/... , /user/.. 开始，便于用 url 控制 (ajax 不适用, 因为只能有一个 url)
         * -
         * 2. /ajax/** 所有的请求，只开放给系统管理员和注册用户，如果系统管理员和注册用户权限有区别，在 ajax 的执行方法里进行区别
         * -
         * 2. 不需要权限的 ajax 请求，做例外处理 security.ignoring().antMatchers("/ajax/tree/ztree/asyncByTreeType.html")
         * -
         * 3. 非 ajax 请求，参考 ajax 规则
         * -
         *
         *
         *
         */

        /**
         * 任何用户都可以访问
         * /error 为 spring boot 保留的全局出错 url ，不受此 spring security 控制
         * http://localhost:8888/base/error 无法直接访问(可以在 CustomErrorController 处改写)
         */
        http.authorizeRequests().antMatchers("/", "/about", "/policies", "/myerror").permitAll(); // /error 不要写在这里
//        http://stackoverflow.com/questions/38747548/spring-boot-disable-error-mapping
//        http://stackoverflow.com/questions/25356781/spring-boot-remove-whitelabel-error-page
//        http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-error-handling
        /**
         *
         */
        http.authorizeRequests()
                .antMatchers("/admin/**", "/user/**").hasAuthority("ADMIN") //  .hasAnyRole("ADMIN","USER")
                /**
                 *所有的 ajax 请求，都需要是认证用户, 避免用户通过 ajax 路径读写信息。Controller 中，ajax 操作，都需要在 /ajax/** 下
                 */
                .antMatchers("/ajax/**").authenticated();
        /**
         *其他未匹配的任何 URL 要求用户进行身份验证后才可以访问
         */
        http.authorizeRequests().anyRequest().authenticated();


        /**
         * 禁止特定的 HttpMethod 访问
         * 等同于在 web.xml 中 <security-constraint></security-constraint>
         * http://www.techstacks.com/howto/disable-http-methods-in-tomcat.html
         * https://myshittycode.com/category/spring/spring-boot/
         * 
         */
//        http.authorizeRequests().
//                antMatchers(HttpMethod.OPTIONS, "/**").denyAll().
//                antMatchers(HttpMethod.PUT, "/**").denyAll().
//                antMatchers(HttpMethod.DELETE, "/**").denyAll().
//                antMatchers(HttpMethod.PATCH, "/**").denyAll().
//                antMatchers(HttpMethod.TRACE, "/**").denyAll();

        /**
         * login
         */
        http.formLogin()
                // 只接受 /login POST 方法的请求并处理 , /login GET 方法会被忽略.请求处理见下面 configure(AuthenticationManagerBuilder auth) 方法的配置
                // loginPage : when authentication is required, redirect the browser to /login , it is GET request (跳转到登陆页面的 url ，只接受 GET 请求，这个 url 仅起跳转到登陆页面的作用)
                // loginProcessingUrl : 登陆页面的 from action url ，只接受 POST 请求。在登录页面设置好即可
                // spring security 验证机制会自动调用下面的 configure(AuthenticationManagerBuilder auth) 方法进行验证
                // 如果 loginProcessingUrl 不写，默认和 loginPage() 方法的参数相同
                // login.jsp 页面需要有 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> , 以满足 spring security 要求
                .loginPage("/login").loginProcessingUrl("/login_process").defaultSuccessUrl("/menu/ajax/index.html").failureUrl("/login?error=abcdedf")
                .usernameParameter("login_email").passwordParameter("login_password")
                .failureHandler(failureHandler).successHandler(successHandler).permitAll();
        /**
         * rememberMe
         */
        http.rememberMe().rememberMeParameter("remember-me"); // remember me 不安全，其 cookies 容易被劫持。有更安全的方案，待查。
        /**
         * logout
         */
        http.logout().logoutSuccessUrl("/login?logout").logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();


        /**
         *session config
         * sessionFixation().changeSessionId() changeSessionId 避免固定会话攻击
         */
        http.sessionManagement()
                .sessionAuthenticationErrorUrl("/login?session=AuthenticationError")//session 验证错误
                .invalidSessionUrl("/login?session=invalid") //如 session 无效，转到的网址
                //1. 限制每个用户只能从两个设备登陆(两个 session)，不同同时登陆多台设备
                //2. session 过期 expired 转到 url
                //3.maxSessionsPreventsLogin(false) :
                //3.1 false ：  超过 maximumSessions ，踢出上一个会话，重新开始新的会话。
                //3.2 注意不能设为 true ，否则登录多个帐号，会不提示信息"会话过多信息"，而直接阻止登录并定位到 failureUrl 定义的地址，从而无法判断问题所在。
                //    只有把 log 设为  <logger name="org.springframework.security.web" level="debug"/> 时才可以看到。
                //自定义 failureHandler 后,  3.2 问题已经解决
                .maximumSessions(1).expiredUrl("/login?session=timeout").maxSessionsPreventsLogin(false);

        /**
         * xxs
         */
        http.headers().xssProtection();

        /**
         * csrf
         */
        http.csrf();

    }


    /**
     * 用于设置不经过 spring controller 和 spring security 拦截的静态资源，直接返回指定的页面.可以提升系统性能
     * 如果和 configure(HttpSecurity http) 配置冲突，本方法的优先级高
     * 此处设置的 url ，spring controller 和 spring security 都不拦截
     * -
     * configure(HttpSecurity http)  .permitAll()
     * configure(WebSecurity security) .ignoring()
     * 的区别 :
     * 前者 spring controller 和 spring security 都拦截
     * 后者 spring controller 和 spring security 都不拦截
     */
    @Override

    public void configure(WebSecurity security) {
        // All of Spring Security will ignore the requests

        /**
         * "/**"  : 测试时打开此开关
         */
        // security.ignoring().antMatchers("/**"); //测试时，不加安全验证
        //

        /**
         * 静态资源不用登录就可以访问，否则首页局无法开放给所有人
         */
        security.ignoring()
                .antMatchers("/resource/**", "/public/**", "/static/**", "/css/**", "/js/**", "/img/**", "**/favicon.ico");

        /**
         * 单独处理的的例外
         */
        //获取树信息，所有人都可以 ？
        security.ignoring()
                .antMatchers("/ajax/tree/ztree/asyncByTreeType.html");

        /**
         *
         */
        //css,js 等文件放在 resources/templates/static/ace 目录下，访问时 url 是 http://localhost/ace/ace.css
        security.ignoring().antMatchers("/ace/**");
        //登陆、注册、找回密码的验证的 ajax ，需要不登陆就能访问，否则无法进行验证
        // 和 ajax/validate 中的 controller 中要呼应
        security.ignoring().antMatchers("/ajax/validate/**");
    }

    /**
     * login 页面 from action url(loginProcessingUrl 设定), POST 类型的 submit , 根据 login 页面传递过来的参数，进行处理的方法
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
        auth.userDetailsService(customUserDetailsService).passwordEncoder(BCryptPassWordUtils.getBCryptPasswordEncoder());
    }


}