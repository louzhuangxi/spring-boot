package com.base.spring.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description : TODO(自定义 filter 演示)
 * User: h819
 * Date: 2015/12/8
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 * 方法一：public class CustomFilter implements Filter
 * 方法二：public class CustomFilter extends OncePerRequestFilter  , spring mvc 中
 * <p>
 * OncePerRequestFilter，顾名思义，它能够确保在一次请求中只通过一次filter，而需要重复的执行。
 * 大家常识上都认为，一次请求本来就只filter一次，为什么还要由此特别限定呢，往往我们的常识和实际的实现并不真的一样，
 * 经过一番资料的查阅，此方法是为了兼容不同的web container，也就是说并不是所有的container都入我们期望的只过滤一次，
 * servlet版本不同，执行过程也不同。
 * <p>
 * spring 中的 filter 都是扩展 OncePerRequestFilter ，比实现接口 implements Filter 更安全一些
 */
public class CustomFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // ((HttpServletRequest) servletRequest).getServletPath()

        try {
            logger.info("in filter : {} ", httpServletRequest.getRequestURL());
//            System.out.println("1 :" + httpServletRequest.getRequestURI());
//            System.out.println("2 :" + httpServletRequest.getQueryString());
//            System.out.println("3 :" + httpServletRequest.getPathInfo());
//            System.out.println("4 :" + httpServletRequest.getServletPath());
//            System.out.println("5 :" + httpServletRequest.getRequestURL());
//            System.out.println("6 :" + JSON.toJSONString(httpServletRequest.getParameterNames()));

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            httpServletRequest.setAttribute("errorMessage", ex);
            httpServletRequest.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}