package com.base.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/12/5
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
// Spring Boot provides an /error mapping by default that handles all errors in a sensible way,
// and it is registered as a ‘global’ error page in the servlet container.
// 改写默认的出错地址及处理
@RestController
public class CustomErrorController implements ErrorController {

    private static final String ERROR_MAPPING = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_MAPPING;
    }

    @RequestMapping(value = ERROR_MAPPING)
    public Map<String, Object> error(HttpServletRequest request) {


        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        String trace = (String) body.get("trace");

        if (trace != null) {
            String[] lines = trace.split("\n\t");
            body.put("trace", lines);
        }
        return body;
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}
