package org.h819.web.spring.jdbc;

/**
 * Description : TODO(构造一个排序条件)
 * User: h819
 * Date: 2017/8/14
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class OrderBean {

    private Direction direction;
    private String property;

    /**
     * @param property  must not be {@literal null} or empty.
     * @param direction must not be {@literal null} or empty
     */
    public OrderBean(String property, Direction direction) {
        this.direction = direction;
        this.property = property;

    }

    /**
     * 只允许带参数初始化
     */
    private OrderBean() {
    }

    public Direction getDirection() {
        return direction;
    }

    public String getProperty() {
        return property;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public enum Direction {
        ASC, DESC;
    }
}
