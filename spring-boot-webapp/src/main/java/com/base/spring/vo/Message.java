package com.base.spring.vo;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/30
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class Message {

    private String name;
    private String value;

    public Message(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
