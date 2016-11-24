package org.h819.web.commons;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/6/2
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class Message {

    private Integer code;
    private String message;

    public Message() {
    }

    public Message(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
