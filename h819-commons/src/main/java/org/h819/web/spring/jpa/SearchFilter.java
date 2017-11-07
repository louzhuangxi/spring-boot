package org.h819.web.spring.jpa;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

public class SearchFilter {


    private String fieldName;
    private Object value;
    private Operator operator; //ISNULL 等判断时, value 不用赋值

    //between 用到，此时上面的 value 不用赋值
    private Object betweenFrom;
    private Object betweenTo;

    /**
     * 禁止无参实例化，通过构造函数赋值，以区别操作类型(between)
     */
    private SearchFilter() {
    }

    /**
     * 构造一个普通的查询条件
     *
     * @param fieldName
     * @param operator
     * @param value
     */
    public SearchFilter(String fieldName, Operator operator, Object value) {
        Assert.isTrue(!operator.equals(Operator.BETWEEN), "非 between 操作，operator 不能为 Operator.BETWEEN"); // 该构造方法，不能用于 between 
        Assert.hasText(fieldName, "fieldName 没有填写");

        //判断 value = null 时的例外
        Operator[] operators = {Operator.BETWEEN, Operator.ISNULL, Operator.ISNOTNULL, Operator.ISEMPTY,
                Operator.ISNOTEMPTY, Operator.ISTRUE, Operator.ISFALSE};
        List<Operator> list = Arrays.asList(operators);
        if (!list.contains(operator))
            Assert.notNull(value, "非 between / is**  操作，value 不能为 null");

        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;

    }

    /**
     * 构造一个 between and 查询条件
     * CriteriaBuilder between  包含两个边界 : betweenFrom <= object <= betweenTo
     *
     * @param fieldName
     * @param operator
     * @param betweenFrom
     * @param betweenTo
     */
    public SearchFilter(String fieldName, Operator operator, Object betweenFrom, Object betweenTo) {

        Assert.isTrue(operator.equals(Operator.BETWEEN), "between 操作，operator 必须为 Operator.BETWEEN");
        Assert.hasText(fieldName, "fieldName 没有填写");
        Assert.noNullElements(new Object[]{betweenFrom, betweenTo}, "between 操作，betweenFrom , betweenTo 均不能为 null");

        this.fieldName = fieldName;
        this.betweenFrom = betweenFrom;
        this.betweenTo = betweenTo;
        this.operator = operator;

    }


    public String getFieldName() {
        return fieldName;
    }


    public Object getValue() {
        return value;
    }


    public Operator getOperator() {
        return operator;
    }

    public Object getBetweenFrom() {
        return betweenFrom;
    }

    public Object getBetweenTo() {
        return betweenTo;
    }

    /**
     * 定义查询条件的操作类型
     */
    public enum Operator {
        EQ,//等于
        NE,//不等于
        LIKE,//包含
        NLIKE,//不包含
        STARTS_WITH,//开始于
        ENDS_WITH, //结束于
        GT,//大于
        GTE, //大于等于
        LT, //小于
        LTE,//小于等于
        IN,  //in(在范围内)
        NIN, //nin(不在范围内)
        BETWEEN, // 之间
        ISNULL,
        ISNOTNULL,
        ISTRUE,
        ISFALSE,
        ISEMPTY,
        ISNOTEMPTY;

    }

    /**
     * 定义查询条件之间的关系s
     */
    public enum Relation {
        AND,
        OR;
    }

}

