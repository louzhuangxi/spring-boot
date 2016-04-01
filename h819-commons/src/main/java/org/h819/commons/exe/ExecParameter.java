package org.h819.commons.exe;

/**
 * Description : TODO(commons exec 的参数)
 * User: h819
 * Date: 2016/1/29
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class ExecParameter {

    private String key;
    private String value;

    public ExecParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
