package org.h819.web.spring.jdbc;

import org.h819.web.spring.vo.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Description : JdbcTemplate工具类
 * User: h819
 * Date: 2015/6/11
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class JdbcUtils {


    private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    @Autowired
    //@Qualifier("oracleDataSource")  // 配置文件中，有了 @Primary ，会默认连接次数据库，不要在指定
    //       JdbcTemplate jdbcTemplate;
    /**
     * 由于是老系统，表之间的关联是原始的，没有用 hibernate 设计，所以表之间的级联关系，无法通过 hibernate 实现 OneToOne 等关联
     * 无论是表关联还是主键关联，都会在原始表中新建字段，这对于老系统改造来说，是不可能的，
     * 只有用 native query 查询来实现。
     * -
     * 经过测试发现， Query queryTotal = entityManager.createNativeQuery(nativeQueryCount,StStandardEntity.class);
     * 此方法，返回的类型，只包含和数据库表对应的 StStandardEntity 中的字段，没有 join 后的其他的表的字段(如 ccsName)，需要进行再次用  @SqlResultSetMappin 做映射处理
     * 见  https://docs.jboss.org/hibernate/orm/5.1/userguide/html_single/chapters/query-native/Native.html
     *  -
     * 例如：
     * 下面的 ccsName 等就不会返回，无法包装入 StStandardEntity ，即使是 StStandardEntity 中有 ccsName 属性和对应的 getter ,setter 方法
     * -
     * 很麻烦，抛弃此方法，直接拼装 sql 查询
     *      -
     * 使用 JdbcTemplate ，也就没有了 Dto 转换问题
     * -
     * 构造一个关联查询的通用语句
     * 左连接 ：left join 意思是包含左边表所有记录，右边所有的匹配的记录，如果没有则用空补齐
     * 本例子是包含 St_Standard 所有记录，如果其他三个表没有记录，则用空白补齐
     * -
     */
    private String queryNativeSql = "select ccs.ccs_name as ccsName ,ics.ics_name as icsName,cl.class_name_cn as classNameCn, st.* from St_Standard st\n" +
            "left join st_ccs_class ccs on st.ccs_code = ccs.ccs_code \n" +
            "left join st_ics_class ics on st.ics_code = ics.ics_code\n" +
            "left join st_class cl on  (st.class_three_code = cl.class_code and cl.class_layer =3) \n" +
            "where \n";

    // count 语句单独拼写，省去很多对计算总数没有用处的条件
    private String queryNativeSqlCount = "select count(*) from St_Standard st \n " +
            "where \n";

    // 使用例子
//    public PageBean<StStandardEntity> findByContainingStandardCode(String standardCode, String classLayer, String timeBegin, String timeEnd, int currentPageNo, int pageSize, String[] sortParameter, SqlUtils.SortDirection sort) {
//
//        String queryNativeSql = this.queryNativeSql + "st.standard_code like '%" + standardCode + "%' " + createParameter(classLayer, timeBegin, timeEnd);
//        String countNativeSql = this.queryNativeSqlCount + "st.standard_code like '%" + standardCode + "%' ";
//        return JdbcUtils.findPageByNativeSqlString(jdbcTemplate, SqlUtils.Dialect.Oracle, queryNativeSql, countNativeSql, currentPageNo, pageSize, sortParameter, sort, StStandardEntity.class);
//
//    }

    /**
     * 构造分页查询，有排序提交件
     * -
     * 利用 spring JdbcTemplate 进行分页查询，BeanPropertyRowMapper 包装
     * 分页需要进行两次查询
     *
     * @param jdbcTemplate    注入 JdbcTemplate 方法
     *                        //@Autowired JdbcTemplate jdbcTemplate;
     *                        -
     *                        如果是多数据源，并且配置时数据源设置了　 @Primary 指向需要连接的数据库，仅需要
     *                        //@Autowired JdbcTemplate jdbcTemplate;
     *                        -
     *                        如果是多数据源，配置时数据源没有设置 @Primary
     *                        //@Autowired //@Qualifier("oracleDataSource") JdbcTemplate jdbcTemplate; 需要指定数据源名称
     * @param dbDialect       数据库类型，不同的数据库生成的分页语句不一样
     * @param queryNativeSql  本地查询语句
     * @param countNativeSql  本地计算总数的语句
     * @param currentPageNo   当前页码，从 1 开始
     * @param pageSize        页大小
     * @param searchParameter 排序参数
     * @param sort            ASC,DESC
     * @param resultClass     返回值类型
     * @return
     */
    public static PageBean findPageByNativeSqlString(final JdbcTemplate jdbcTemplate, final SqlUtils.Dialect dbDialect, final String queryNativeSql, final String countNativeSql, int currentPageNo, int pageSize, String[] searchParameter, SqlUtils.SortDirection sort, Class resultClass) {

        if (currentPageNo < 1)
            throw new IllegalArgumentException("currentPageNo : 起始页应从 1 开始。");

        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize : 页大小不能小于 0");

        if (!countNativeSql.toLowerCase().contains("count"))
            throw new IllegalArgumentException("queryNativeSql 和 countNativeSql 参数顺序不对");


        String queryNativeSqlString = SqlUtils.createNativePageSqlString(dbDialect, queryNativeSql, currentPageNo, pageSize, searchParameter, sort);

        logger.info("countNativeSql : \n {} ", countNativeSql);
        logger.info("queryPageNativeSql : \n {} ", queryNativeSqlString);


        // 计算总数
        final int totalRecordsSize = jdbcTemplate.queryForObject(countNativeSql, Integer.class);
//        logger.info("totalRecordsSize : " + totalRecordsSize);
        List content = jdbcTemplate.query(queryNativeSqlString, new BeanPropertyRowMapper(resultClass));
        return new PageBean(pageSize, currentPageNo, totalRecordsSize, content);
    }

    /**
     * 分页，同上，无排序条件
     *
     * @param jdbcTemplate
     * @param dbDialect
     * @param queryNativeSql
     * @param countNativeSql
     * @param currentPageNo
     * @param pageSize
     * @param resultClass
     * @return
     */
    public static PageBean findPageByNativeSqlString(final JdbcTemplate jdbcTemplate, final SqlUtils.Dialect dbDialect, final String queryNativeSql, final String countNativeSql, int currentPageNo, int pageSize, Class resultClass) {
        return findPageByNativeSqlString(jdbcTemplate, dbDialect, queryNativeSql, countNativeSql, currentPageNo, pageSize, null, null, resultClass);
    }

    /**
     * 构造查询语句，不分页，排序
     *
     * @param jdbcTemplate
     * @param queryNativeSql
     * @param searchParameter
     * @param sort
     * @param resultClass
     * @return
     */
    public static <T> List<T> findByNativeSqlString(final JdbcTemplate jdbcTemplate, final String queryNativeSql, String[] searchParameter, SqlUtils.SortDirection sort, Class resultClass) {

        String sqlString = SqlUtils.createSqlString(queryNativeSql, searchParameter, sort);
        logger.info("queryNativeSql : \n {} ", sqlString);
        return jdbcTemplate.query(sqlString, new BeanPropertyRowMapper(resultClass));

    }

    /**
     * 同上，构造查询语句，不分页，不排序
     *
     * @param jdbcTemplate
     * @param queryNativeSql
     * @param resultClass
     * @return
     */
    public static <T> List<T> findByNativeSqlString(final JdbcTemplate jdbcTemplate, final String queryNativeSql, Class resultClass) {
        return jdbcTemplate.query(queryNativeSql, new BeanPropertyRowMapper(resultClass));

    }
}