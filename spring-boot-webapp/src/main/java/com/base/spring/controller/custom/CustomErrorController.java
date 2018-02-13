package com.base.spring.controller.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2018/1/5
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
// Spring Boot provides an /error mapping by default that handles all errors in a sensible way,
// and it is registered as a ‘global’ error page in the servlet container.
// 改写默认的出错返回的 url 、处理
@Slf4j
public class CustomErrorController extends AbstractErrorController {

    private static final String ERROR_PATH = "/error";   // 自定义的出错后返回的地址

    @Autowired
    ObjectMapper objectMapper;

    public CustomErrorController() {
        super(new DefaultErrorAttributes());
    }

    @RequestMapping(ERROR_PATH)
    public ModelAndView getErrorPath(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, false));

        Throwable cause = getCause(request);
        int status = (Integer) model.get("status");
        //错误信息
        String message = (String) model.get("message");
        //友好提示
        String errorMessage = getErrorMessage(cause);
        //后台打印日志信息方方便查错
        log.info(message, cause);
        response.setStatus(status);
        if (!isJsonRequest(request)) {
            ModelAndView view = new ModelAndView("/error.html");
            view.addAllObjects(model);
            view.addObject("status", status);
            view.addObject("errorMessage", errorMessage);
            view.addObject("cause", cause);
            return view;
        } else {
            Map error = new HashMap();
            error.put("success", false);
            error.put("errorMessage", getErrorMessage(cause));
            error.put("message", message);
            writeJson(response, error);
            return null;
        }


    }

    protected boolean isJsonRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.endsWith(".json")) {
            return true;
        } else {
            return (request.getHeader("accept").contains("application/json") || (request.getHeader("X-Requested-With") != null
                    && request.getHeader("X-Requested-With").contains("XMLHttpRequest")));
        }
    }

    protected void writeJson(HttpServletResponse response, Map error) {
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (IOException e) {
            // ignore
        }
    }

    protected String getErrorMessage(Throwable ex) {
        /*不给前端显示详细错误*/
        return "服务器错误,请联系管理员";
    }

    protected Throwable getCause(HttpServletRequest request) {
        Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
        }
        return error;
    }


    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return ERROR_PATH;
    }
}
