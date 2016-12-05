package com.base.spring.vo;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/12/5
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class CustomErrorResponseMessage {

    private int value;
    private String Message;
    private HttpStatus status;

    public CustomErrorResponseMessage(HttpServletRequest request, String message) {
        Message = message;
        this.status = getStatus(request);
        this.value = status.value();

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
