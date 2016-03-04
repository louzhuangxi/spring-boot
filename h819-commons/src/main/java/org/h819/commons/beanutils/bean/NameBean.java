package org.h819.commons.beanutils.bean;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class NameBean  {
    private String name;
    private Integer age;

    public NameBean() {
    }

    public NameBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
