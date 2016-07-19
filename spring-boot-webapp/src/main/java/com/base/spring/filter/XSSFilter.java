package com.base.spring.filter;

import com.base.spring.filter.xss.XSSRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description : TODO(防御 XSS 攻击，利用 filter , 对每个输入的 request ，都进行安全转码)
 * User: h819
 * Date: 2016/5/18
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 * see https://dzone.com/articles/stronger-anti-cross-site
 */
public class XSSFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new XSSRequestWrapper(httpServletRequest), httpServletResponse);
    }

}