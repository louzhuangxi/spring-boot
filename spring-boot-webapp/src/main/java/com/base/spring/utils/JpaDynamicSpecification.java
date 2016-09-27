/**
 * ****************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * 参考 springside 4 项目，改动并做了扩展
 * *****************************************************************************
 */
package com.base.spring.utils;

import org.h819.web.spring.jpa.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.*;


/**
 * Description : TODO(创建动态查询语句 Specification ，有了 Specification 之后，可以利用 JPA 进行查询。Specification 就是各种查询条件集合)
 * Description : TODO(Specification ，实现其接口 toPredicate 方法即可 ) Querydsl 也可以 写出动态查询
 * Description : TODO( Querydsl 也可以写出动态查询，有时间写一个 Querydsl 版本)
 * User: h819
 * Date: 2015-5-3
 * Time: 下午9:20
 * To change this template use File | Settings | File Templates.
 */

//http://www.baeldung.com/spring-rest-api-query-search-language-tutorial
//另外一个参考的例子：http://www.baeldung.com/spring-rest-api-query-search-language-tutorial
//https://github.com/eugenp/tutorials/blob/master/spring-security-rest-full/src/main/java/org/baeldung/persistence/dao/UserSpecification.java
//   Querydsl 改造 http://www.baeldung.com/rest-api-search-language-spring-data-querydsl
//https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
//http://blog.csdn.net/RO_wsy/article/details/51345875
public class JpaDynamicSpecification {

    private static Logger logger = LoggerFactory.getLogger(JpaDynamicSpecification.class);


    private Specification specification;


    public JpaDynamicSpecification() {
        this.specification = Specifications.where(null);
    }


    /**
     * @param searchFilter 查询条件
     *                     根据于查询对象的属性，构造 SearchFilter
     *                     <p/>
     *                     假设查询条件 level 是一个对象的字符串属性，构造为 SearchFilter searchFilter = new SearchFilter("level", SearchFilter.Operator.EQ, "1");
     *                     假设查询条件 user 是一个对象，构造为 searchFilters.add(new SearchFilter("user", SearchFilter.Operator.EQ, userEntity)); // userEntity 为一个对象。
     *                     级联查询时
     *                     new SearchFilter("parent.name", SearchFilter.Operator.EQ, "pname") ，被查询对象的 parent 属性是一个对象，该对象的 name 属性等于 pname
     * @return
     */
    private <T> Specification bySearchFilter(final SearchFilter searchFilter) {

        return new Specification<T>() {
            /**
             *
             * @param root   查询中的条件表达式
             * @param query  条件查询设计器
             * @param builder 条件查询构造器
             * @return
             */
            @Override
            public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

                //即被查询的属性代表的对象类型，可以是对象，也可以是字符串。不同的比较方法，要求的对象类型不同
                //此处为自动获取
                Class<?> javaType = getPathJavaType(root, searchFilter.fieldName);
                //  logger.info("javaType : {}", javaType);
                // logic operator
                switch (searchFilter.operator) {

                    case EQ:
                        return builder.equal(translateNestedPath(javaType, root, searchFilter.fieldName), searchFilter.value);

                    case NE:
                        return builder.notEqual(translateNestedPath(javaType, root, searchFilter.fieldName), searchFilter.value);

                    case LIKE: // like,notlike 操作要求：属性代表的对象， String 类型，才可以比较 。(只有字符串才可以 like)

                    {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.fieldName), "%" + searchFilter.value + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case NLIKE: {
                        if (javaType.equals(String.class))
                            return builder.notLike(translateNestedPath(String.class, root, searchFilter.fieldName), "%" + searchFilter.value + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("not like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case STARTS_WITH:

                    {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.fieldName), searchFilter.value + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("STARTS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }


                    case ENDS_WITH: {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.fieldName), "%" + searchFilter.value);
                        else
                            throw new org.springframework.transaction.TransactionSystemException("ENDS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }


                    case GT: //great,less 操作要求：属性代表的对象，应该可以进行比较，即实现了 Comparable 才可以，这符合 java 对象比较的原则

                    {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThan((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.fieldName), (Comparable) searchFilter.value);
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! greaterThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case GTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThanOrEqualTo((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.fieldName), (Comparable) searchFilter.value);
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! greaterThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }

                    case LT: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThan((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.fieldName), (Comparable) searchFilter.value);
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! lessThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case LTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThanOrEqualTo((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.fieldName), (Comparable) searchFilter.value);
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! lessThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case NN:  // 此时只需要 searchFilter.fieldName ，searchFilter.value 用不到
                        return builder.isNotNull(translateNestedPath(javaType, root, searchFilter.fieldName));
                    case IN://IN , NIN  操作要求：属性代表的对象，在集合中，对象应该可以比较

                        /**
                         * 构造单个属性的 IN 查询 ，相当于  @Query("select e from InfoEntity e where e.province in ?1")
                         * 此时： attribute 为 "province" ,  values 为 e.province 对象的集合
                         * 多个属性在不同集合中的 IN 查询，用 join 方法连接
                         *
                         * @param searchFilter.fieldName 实体中的属性名称。对应上述列子，应是 InfoEntity 中的 province  属性。
                         *                  如果集合为空，会抛出异常
                         *                  处理办法是：在集合中放入一个不会影响程序运行结果的元素，进行比较。
                         *                  例如集合元素是对象 id ，long 类型，则放入 -1 ，id 不会是负数，所以不会影响程序运行。
                         *                  注意：
                         *                  1. 不能是级联属性，只能比较实体中的直接属性，例如 province.website 就不行。
                         *                  2. 如果遇到比较的是级联属性，则通过其他方法查询级联属性后再比较。如通过查询 website -> province ,之后还是直接比较 province
                         * @param searchFilter.value    用于比较的对象集合。 对应上述列子，应该是 InfoEntity 中的 e.province 对象的集合
                         * @param <T>
                         * @return
                         */

                        return translateNestedPath(javaType, root, searchFilter.fieldName).in(searchFilter.value);
                    case NIN:
                        return translateNestedPath(javaType, root, searchFilter.fieldName).in(searchFilter.value).not();
                    default:
                        return null;

                }

            }
        };


    }


    public JpaDynamicSpecification and(final SearchFilter searchFilter) {

        this.specification = Specifications.where(this.specification).and(bySearchFilter(searchFilter));
        return this;
    }

    public JpaDynamicSpecification or(final SearchFilter searchFilter) {
        this.specification = Specifications.where(this.specification).or(bySearchFilter(searchFilter));
        return this;
    }

    public <T> Specification<T> build() {
        return specification;
    }

    /**
     * 转换 path
     * <p/>
     * 当 jpa 对象有级联关系时，被级联的对象的某个属性作为查询条件构建 Predicate ，如
     * 查询属性为 tree.parent.name (tree 对象中的 parent 对象 的 name 属性)
     * 构建的 Predicate 中的 Path 不是
     * return build.equal(root.get("tree.parent.name"), "value");
     * 而应该是
     * return cb.equal((Path<String>) ((Path<Area>) root.get("tree")).get("parent").get("name"),"value");
     *
     * @param resultType root.get("tree").get("parent").get("name") 的返回值，即被查询的属性 name 代表的对象类型，可以是对象，也可以是字符串
     * @param root
     * @param filter
     * @param <T>
     * @param <R>
     * @return
     * @see "http://stackoverflow.com/questions/13246959/jpa-specification-with-onetoone-relation"
     */
    private static <T, R> Path<R> translateNestedPath(Class<R> resultType, Root<T> root, String filter) {

        String[] names = filter.split("\\.");
        Path nestedPath = root;
        for (String subPath : names)
            nestedPath = nestedPath.get(subPath);
        return (Path<R>) nestedPath;
    }


    /**
     * 取得属性代表的对象的类型
     *
     * @param root
     * @param filter
     * @param <T>
     * @return
     */
    private static <T> Class getPathJavaType(Root<T> root, String filter) {
        /**
         *取得最终的属性名称
         */
        String[] names = filter.split("\\.");
        Path nestedPath = root;
        for (String subPath : names)
            nestedPath = nestedPath.get(subPath);
        return nestedPath.getJavaType();
    }


}
