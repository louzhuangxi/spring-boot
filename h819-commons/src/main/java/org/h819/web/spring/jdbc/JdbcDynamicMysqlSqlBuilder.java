package org.h819.web.spring.jdbc;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * Description : TODO(创建动态查询语句 Specification ，有了 Specification 之后，可以利用 JPA 进行查询。Specification 就是各种查询条件集合)
 * Description : TODO(Specification ，实现其接口 toPredicate 方法即可 )
 * User: h819
 * Date: 2016-9-28
 * Time: 下午13:20
 * To change this template use File | Settings | File Templates.
 */

/**
 * 构造动态 sql
 */
public class JdbcDynamicMysqlSqlBuilder implements JdbcDynamicSqlBuilder {

    //  private static Logger logger = LoggerFactory.getLogger(JpaDynamicSpecificationBuilder.class);
    private String specification;

    public JdbcDynamicMysqlSqlBuilder() {
        this.specification = "";
    }

    /**
     * 条件 and
     * <p>
     * 为了避免方法名称重复，分别用了 Iterable 和 Collection
     *
     * @param searchFilter
     * @return
     */
    public JdbcDynamicMysqlSqlBuilder and(final JdbcSearchFilter searchFilter) {
        this.specification = specification + " and " + bySearchFilter(searchFilter);
        return this;
    }

    public JdbcDynamicMysqlSqlBuilder and(final JdbcSearchFilter... searchFilters) {
        return and(Arrays.asList(searchFilters));
    }

    public JdbcDynamicMysqlSqlBuilder and(final Iterable<JdbcSearchFilter> searchFilters) {
        for (JdbcSearchFilter filter : searchFilters)
            this.specification = specification + and(searchFilters);
        return this;
    }

    /**
     * 条件 or
     *
     * @param searchFilter
     * @return
     */
    public JdbcDynamicMysqlSqlBuilder or(final JdbcSearchFilter searchFilter) {
        this.specification = specification + " or " + bySearchFilter(searchFilter);
        return this;
    }

    public JdbcDynamicMysqlSqlBuilder or(final JdbcSearchFilter... searchFilters) {
        return or(Arrays.asList(searchFilters));
    }

    public JdbcDynamicMysqlSqlBuilder or(final Iterable<JdbcSearchFilter> searchFilters) {
        for (JdbcSearchFilter filter : searchFilters)
            this.specification = specification + or(searchFilters);
        return this;
    }

    /**
     * 条件 not
     *
     * @return
     */
    public JdbcDynamicMysqlSqlBuilder not() {

        return this;
    }

    public String build() {
        return this.specification;
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
    private String bySearchFilter(final JdbcSearchFilter searchFilter) {

        StringBuilder builder = new StringBuilder();
        builder.append(searchFilter.getTableName());
        builder.append(".");

        final String sql = searchFilter.getTableName() + "." + searchFilter.getFieldName() + "{0}" + searchFilter.getValue();

        switch (searchFilter.getOperator()) {

            /**
             * 日期类型？
             */
            case EQ:
                if (String.class.isAssignableFrom(searchFilter.getValue().getClass()))
                    return builder.append("=").append(searchFilter.getValue()).toString();
                else if (Date.class.isAssignableFrom(searchFilter.getValue().getClass()))
                    return builder.append("=").append(searchFilter.getValue()).toString();

            case NE:
                return MessageFormat.format(sql, "!=");


            default:
                return specification;

        }
    }
}