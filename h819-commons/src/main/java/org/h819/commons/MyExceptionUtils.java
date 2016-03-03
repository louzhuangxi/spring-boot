package org.h819.commons;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/8/18
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class MyExceptionUtils extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public MyExceptionUtils(Throwable e) {
        super(e.getMessage(), e);
    }

    public MyExceptionUtils(String message) {
        super(message);
    }

    public MyExceptionUtils(String messageTemplate, Object... params) {
        super(messageTemplate.format(messageTemplate, params));
    }

    public MyExceptionUtils(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MyExceptionUtils(Throwable throwable, String messageTemplate, Object... params) {
        super(messageTemplate.format(messageTemplate, params), throwable);
    }

}
