package com.base.spring.custom;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Description : TODO(自定义 filter 演示)
 * User: h819
 * Date: 2015/12/8
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class CustomFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // ((HttpServletRequest) servletRequest).getServletPath()

        try {
            System.out.println("in filter .... " + request.getRequestURL());
//            System.out.println("1 :" + request.getRequestURI());
//            System.out.println("2 :" + request.getQueryString());
//            System.out.println("3 :" + request.getPathInfo());
//            System.out.println("4 :" + request.getServletPath());
//            System.out.println("5 :" + request.getRequestURL());
//            System.out.println("6 :" + JSON.toJSONString(request.getParameterNames()));
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            servletRequest.setAttribute("errorMessage", ex);
            servletRequest.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp").forward(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}