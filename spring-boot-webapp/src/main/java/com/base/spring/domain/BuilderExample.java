package com.base.spring.domain;

/**
 * Description : TODO(Builder 模式例子)
 * User: h819
 * Date: 2016/6/15
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class BuilderExample {

    private final int age;
    private final int safeID;
    private final String name;
    private final String address;

    /**
     * 下面的内部类用到，用于传递赋值信息
     *
     * @param builder
     */
    private BuilderExample(Builder builder) {
        age = builder.age;
        safeID = builder.safeID;
        name = builder.name;
        address = builder.address;

    }

    public static void main(String[] agrs) {


        System.out.println(new Builder().name("nametest").age(11).build());
    }

    @Override
    public String toString() {
        return "BuilderExample{" +
                "age=" + age +
                ", safeID=" + safeID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public int getAge() {
        return age;
    }

    public int getSafeID() {
        return safeID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public static class Builder {
        private int age = 0;
        private int safeID = 0;
        private String name = null;
        private String address = null;

        // 构建的步骤
        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int val) {
            age = val;
            return this;
        }

        public Builder safeID(int val) {
            safeID = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        /**
         * 传递内部类的赋值信息，到上层类
         *
         * @return
         */
        public BuilderExample build() { // 构建，返回一个新对象
            return new BuilderExample(this);
        }
    }
}
