/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * 参考 springside 4 项目，改动并做了扩展.  SearchFilter 类做了简化
 *******************************************************************************/
package org.h819.web.spring.jpa;

public class SearchFilter {

    public String fieldName;
    public Object value;
    public Operator operator;

    /**
     * 禁止无参实例化
     */
    private SearchFilter() {
    }

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
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
        NN, //notNull
        IN,  //in(在范围内)
        NIN; //nin(不在范围内)

    }

    /**
     * 定义查询条件之间的关系s
     */
    public enum Relation {
        AND,
        OR;
    }
}

