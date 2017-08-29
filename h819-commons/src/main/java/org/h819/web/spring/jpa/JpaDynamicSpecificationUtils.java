/**
 * ****************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * 参考 springside 4 项目，改动并做了扩展
 * *****************************************************************************
 */
package org.h819.web.spring.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


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
@Deprecated // 用 builder 代替
public class JpaDynamicSpecificationUtils {

    //private static final Logger logger = LoggerFactory.getLogger(JpaDynamicSpecificationUtils.class);

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
    public static <T> Specification bySearchFilter(final SearchFilter searchFilter) {

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
                Class<?> javaType = getPathJavaType(root, searchFilter.getFieldName());
                //  logger.info("javaType : {}", javaType);
                // logic operator
                switch (searchFilter.getOperator()) {

                    case EQ:
                        return builder.equal(translateNestedPath(javaType, root, searchFilter.getFieldName()), searchFilter.getValue());

                    case NE:
                        return builder.notEqual(translateNestedPath(javaType, root, searchFilter.getFieldName()), searchFilter.getValue());

                    case LIKE: // like,notlike 操作要求：属性代表的对象， String 类型，才可以比较 。(只有字符串才可以 like)

                    {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.getFieldName()), "%" + searchFilter.getValue() + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case NLIKE: {
                        if (javaType.equals(String.class))
                            return builder.notLike(translateNestedPath(String.class, root, searchFilter.getFieldName()), "%" + searchFilter.getValue() + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("not like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case STARTS_WITH:

                    {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.getFieldName()), searchFilter.getValue() + "%");
                        else
                            throw new org.springframework.transaction.TransactionSystemException("STARTS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }


                    case ENDS_WITH: {
                        if (javaType.equals(String.class))
                            return builder.like(translateNestedPath(String.class, root, searchFilter.getFieldName()), "%" + searchFilter.getValue());
                        else
                            throw new org.springframework.transaction.TransactionSystemException("ENDS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }


                    case GT: //great,less 操作要求：属性代表的对象，应该可以进行比较，即实现了 Comparable 才可以，这符合 java 对象比较的原则

                    {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThan((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.getFieldName()), (Comparable) searchFilter.getValue());
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! greaterThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case GTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThanOrEqualTo((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.getFieldName()), (Comparable) searchFilter.getValue());
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! greaterThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }

                    case LT: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThan((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.getFieldName()), (Comparable) searchFilter.getValue());
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! lessThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case LTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThanOrEqualTo((Expression<? extends Comparable>) translateNestedPath(javaType, root, searchFilter.getFieldName()), (Comparable) searchFilter.getValue());
                        else
                            throw new org.springframework.transaction.TransactionSystemException("不能比较! lessThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case NN:  // 此时只需要 searchFilter.getFieldName() ，searchFilter.getValue() 用不到
                        return builder.isNotNull(translateNestedPath(javaType, root, searchFilter.getFieldName()));
                    case IN://IN , NIN  操作要求：属性代表的对象，在集合中，对象应该可以比较

                        /**
                         * 构造单个属性的 IN 查询 ，相当于  @Query("select e from InfoEntity e where e.province in ?1")
                         * 此时： attribute 为 "province" ,  values 为 e.province 对象的集合
                         * 多个属性在不同集合中的 IN 查询，用 join 方法连接
                         *
                         * @param searchFilter.getFieldName() 实体中的属性名称。对应上述列子，应是 InfoEntity 中的 province  属性。
                         *                  如果集合为空，会抛出异常
                         *                  处理办法是：在集合中放入一个不会影响程序运行结果的元素，进行比较。
                         *                  例如集合元素是对象 id ，long 类型，则放入 -1 ，id 不会是负数，所以不会影响程序运行。
                         *                  注意：
                         *                  1. 不能是级联属性，只能比较实体中的直接属性，例如 province.website 就不行。
                         *                  2. 如果遇到比较的是级联属性，则通过其他方法查询级联属性后再比较。如通过查询 website -> province ,之后还是直接比较 province
                         * @param searchFilter.getValue()    用于比较的对象集合。 对应上述列子，应该是 InfoEntity 中的 e.province 对象的集合
                         * @param <T>
                         * @return
                         */

                        return translateNestedPath(javaType, root, searchFilter.getFieldName()).in(searchFilter.getValue());
                    case NIN:
                        return translateNestedPath(javaType, root, searchFilter.getFieldName()).in(searchFilter.getValue()).not();
                    default:
                        return null;

                }

            }
        };
    }

    /**
     * 构造 between 查询，包含 start , end
     *
     * @param attribute
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    //Between 需要两个变量，操作特殊，不放入 bySearchFilter 方法
    public static <T> Specification<T> byBetweenOperator(final String attribute, final Object start, final Object end) {

        //
        //  builder.between() 没有实验成功

        //方法二 :构造查询条件
        return joinSpecification(
                SearchFilter.Relation.AND,
                bySearchFilter(new SearchFilter(attribute, SearchFilter.Operator.GTE, start)),
                bySearchFilter(new SearchFilter(attribute, SearchFilter.Operator.LTE, end))
        );
    }

    /**
     * 查询条件的相反条件
     *
     * @param specs 查询条件
     * @param <T>
     * @return
     */
    public static <T> Specification<T> byNotSpecification(Specification<T> specs) {
        return Specifications.not(specs);

    }

    /**
     * 多个 Specification 组合
     *
     * @param specifications
     * @param relation       条件之间的关系，只能有一种 , SearchFilter.Relation.AND 或者 SearchFilter.Relation.OR。
     *                       如果有 AND 和 OR 的关系是，可以把 specifications 拆为 两个两个组合，这样就可以设置多样的关联关系
     * @param <T>
     * @return
     */
    public static <T> Specification<T> joinSpecification(final SearchFilter.Relation relation, final Specification... specifications) {

        Assert.isTrue(relation.equals(SearchFilter.Relation.AND) || relation.equals(SearchFilter.Relation.OR), "relation must be \"AND | OR  \"");

        //构造第一个 Specification ，这么做，只是为了使用 Specifications 方法，Specifications 无法构造一个空的  Specification
        Specification specificationFinal = Specifications.where(specifications[0]);
        //去掉第一个
        List<Specification> subSpecification = Arrays.asList(specifications).subList(1, specifications.length);

        //在第一个的基础上，继续构造
        for (Specification spec : subSpecification) {
            if (relation.equals(SearchFilter.Relation.AND))
                specificationFinal = Specifications.where(spec).and(specificationFinal);
            else specificationFinal = Specifications.where(spec).or(specificationFinal);

        }
        return specificationFinal;
    }

    /**
     * 同上，集合参数形式
     *
     * @param specifications
     * @param relation
     * @param <T>
     * @return
     */
    public static <T> Specification<T> joinSpecification(final SearchFilter.Relation relation, final Collection<Specification> specifications) {
        return joinSpecification(relation, specifications.toArray(new Specification[specifications.size()]));
    }

    /**
     * 多个 SearchFilter 创建 Specification
     *
     * @param relation
     * @param searchFilters
     * @param <T>
     * @return
     */
    public static <T> Specification<T> joinSearchFilter(final SearchFilter.Relation relation, final SearchFilter... searchFilters) {

        Collection<Specification> specifications = new ArrayList<Specification>(searchFilters.length);
        for (SearchFilter filter : searchFilters)
            specifications.add(bySearchFilter(filter));

        return joinSpecification(relation, specifications);
    }

    /**
     * 多个 SearchFilter 创建 Specification
     *
     * @param relation
     * @param searchFilters
     * @param <T>
     * @return
     */
    public static <T> Specification<T> joinSearchFilter(final SearchFilter.Relation relation, final Collection<SearchFilter> searchFilters) {

        return joinSearchFilter(relation, searchFilters.toArray(new SearchFilter[searchFilters.size()]));
    }


    /**
     * 转换 path
     * <p/>
     * 当 jpa 对象有级联关系时，被级联的对象的某个属性作为查询条件构建 Predicate ，如
     * 查询属性为 tree.parent.name (tree 对象中的 parent 对象 的 name 属性)
     * 构建的 Predicate 中的 Path 不是
     * return builder.equal(root.get("tree.parent.name"), "value");
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
