package org.h819.web.spring.jpa;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/8/5
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private String name;
    private int age;
    private boolean sex;

    public User(String name, int age, boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
