package com.base.spring.filter;

import com.base.spring.filter.xss.XSSRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Description : TODO(防御 XSS 攻击，每个输入的 request ，都进行安全转码)
 * User: h819
 * Date: 2016/5/18
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 * see https://dzone.com/articles/stronger-anti-cross-site
 */
public class XSSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }

}