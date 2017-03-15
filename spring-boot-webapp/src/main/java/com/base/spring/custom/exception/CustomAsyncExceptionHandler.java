package com.base.spring.custom.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/3/10
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.err.println("Exception message - " + throwable.getMessage());
        System.err.println("Method name - " + method.getName());
        for (Object param : obj) {
            System.err.println("Parameter value - " + param);
        }
    }
}
