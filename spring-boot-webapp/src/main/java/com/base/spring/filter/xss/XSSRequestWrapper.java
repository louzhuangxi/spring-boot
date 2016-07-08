package com.base.spring.filter.xss;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/5/18
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
//https://dzone.com/articles/stronger-anti-cross-site
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    private static Logger logger = LoggerFactory.getLogger(XSSRequestWrapper.class);


    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            value = ESAPI.encoder().canonicalize(value);

            //html
            //  StringEscapeUtils 过滤的没有 ESAPI 完善
            //value = StringEscapeUtils.escapeHtml3(value);  // escapeHtml4 包含  escapeHtml3 的功能
//            value = StringEscapeUtils.escapeHtml4(value);
//            //javaScript
//            value = StringEscapeUtils.escapeEcmaScript(value);

        }
        return value;
    }
}