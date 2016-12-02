package com.base.spring.custom;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/30
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */


//https://www.qyh.me/space/mhlx/blog/158 看看在这个
//http://code.qtuba.com/article-60256.html
//@Component(value = "customAuthenticationFailureHandler")   //不启用
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 自定义一个错误处理器
     * .failureUrl("/login?error") 只能返回一个参数，如果想要提示不同的登陆错误，需要自定义一个错误处理器
     * 本来想实现用户名不对/密码不对/服务器内部错误的不同提示
     * 但是发现如果发生错误，返回的信息都是 BadCredentialsException 类型，无法区分
     * 这个类保留在这里，以作提示，等 spring security 升级之后，是否能改善。
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

//            httpServletResponse.sendError(403, "Access Denied");
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed.");
//            httpServletResponse.sendRedirect("/login/fail?page="+httpServletRequest.getParameter("page"));

        if (e instanceof BadCredentialsException) {
            //System.out.println("BadCredentialsException");
            httpServletResponse.sendRedirect("/login?error=用户名或密码不存在"); //跳转到 /login GET 处理
        }

        //Thrown if an UserDetailsService implementation cannot locate a User by its username.
        if (e instanceof UsernameNotFoundException) {
            // System.out.println("UsernameNotFoundException");
            // httpServletResponse.sendRedirect("/login?error=用户名不存在");
        }
    }
}
