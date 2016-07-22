package org.examples.j2se.domain;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/2/15
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class Person {

    private String name;
    private Integer age;
    private Integer index;

    public Person(String name, Integer age, Integer index) {
        this.name = name;
        this.age = age;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
