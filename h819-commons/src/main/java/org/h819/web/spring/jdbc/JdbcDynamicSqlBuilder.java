package org.h819.web.spring.jdbc;

/**
 * Description : TODO(创建动态查询语句 Specification ，有了 Specification 之后，可以利用 JPA 进行查询。Specification 就是各种查询条件集合)
 * Description : TODO(Specification ，实现其接口 toPredicate 方法即可 )
 * User: h819
 * Date: 2016-9-28
 * Time: 下午13:20
 * To change this template use File | Settings | File Templates.
 */
public interface JdbcDynamicSqlBuilder {


    /**
     * 条件 and
     * <p>
     * 为了避免方法名称重复，分别用了 Iterable 和 Collection
     *
     * @param searchFilter
     * @return
     */
    public JdbcDynamicSqlBuilder and(final JdbcSearchFilter searchFilter);

    public JdbcDynamicSqlBuilder and(final JdbcSearchFilter... searchFilters);

    public JdbcDynamicSqlBuilder and(final Iterable<JdbcSearchFilter> searchFilters);


    /**
     * 条件 or
     *
     * @param searchFilter
     * @return
     */
    public JdbcDynamicSqlBuilder or(final JdbcSearchFilter searchFilter);

    public JdbcDynamicSqlBuilder or(final JdbcSearchFilter... searchFilters);

    public JdbcDynamicSqlBuilder or(final Iterable<JdbcSearchFilter> searchFilters);


    /**
     * 条件 not
     *
     * @return
     */
    public JdbcDynamicSqlBuilder not();

    public String build();
}
