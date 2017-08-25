package org.h819.web.spring.jpa;

import org.h819.web.spring.vo.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Description : JQL 工具类 ，参数绑定方式，可以避免 sql 注入
 * User: h819
 * Date: 2015/6/11
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
@Deprecated// 麻烦，此处仅为示例，未测试是否好用
// 解决方案：
// Hibernate 设计的系统，用 JPA
// 非 Hibernate 设计的系统，用 JdbcTemplate
// 参考
//https://github.com/nejads/Articles/blob/master/paging.md
//http://www.baeldung.com/jpa-pagination
public class JqlUtils {

    private static final Logger logger = LoggerFactory.getLogger(JqlUtils.class);
    /**
     * 占位符只能用于 value ，不能用于列名(column)
     * 所以 order by ? ,? asc 不可以
     */

    //spring jdbc ，占位符方式 like 表达式的写法固定，只有 like  特殊，其他的和普通 sql 相同
    //where name like '%中国%' ，转换为占位符方式，相当于
    //where name like '%'||?||'%'
    public static String BIND_LIKE_STRING = " '%'||?||'%' ";

    //其他的例子
    //= ：  name =?
    //between :  act_time between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd')

    //@Autowired
    //@Qualifier("oracleDataSource")  // 配置文件中，有了 @Primary ，会默认连接次数据库，不要在指定
    //       JdbcTemplate jdbcTemplate;


    /**
     * 利用 JQL 语句进行分页查询
     * JQL 需在 Hibernate 环境中，entityManager 管理 Entity（表）
     * 分页需要进行两次查询
     * 排序条件不在这里维护，在 JQL 语句中。计算总数的语句和查询语句不同，计算总数的语句可以简化很多条件，所以分开写
     *
     * @param em            注入 JdbcTemplate 方法
     *                      //@Autowired EntityManager em;
     *                      -
     *                      如果是多数据源，在可以指定数据源
     * @param queryJql      查询 JQL 语句 ，和不分页时相同，含 Order 如
     *                      String query = "select s.* from Something s "
     *                      + "join somethingelse selse on selse.id = s.fk_somethingelse "
     *                      + "where selse.id = :somethingElseId "
     *                      + "order by selse.date";
     * @param queryArgs     arguments to bind to the query ，查询时，绑定在 queryJql 上的参数，按照位置对应
     *                      按照占位参数的位置，逐个对应 Object[] 中的参数， query.setParameter(i, queryArgs[i]);
     *                      -
     *                      (leaving it to the PreparedStatement to guess the corresponding SQL type ，是 Object 类型，系统可以自动匹配成 sql 类型)
     *                      无参数时，传入 new Objects[]{} 空数组即可
     *                      占位符方式，可以避免 sql 注入  Queries with Named Parameters
     * @param countJql      计算总数的 JQL 语句 , 如
     *                      String countQuery = "select count(s.*) from Something s "
     *                      + "join somethingelse selse on selse.id = s.fk_somethingelse "
     *                      + "where selse.id = :somethingElseId ";
     * @param countArgs     计算总数时，绑定 countJql 上的参数
     * @param currentPageNo 当前页码，从 1 开始
     * @param pageSize      页大小
     * @param resultClass   自定义返回值类型，一般为 Bean
     * @param <T>
     * @return
     */
    private static <T> PageBean<T> queryPageByJQLString(final EntityManager em,
                                                        final String queryJql, Object[] queryArgs,
                                                        final String countJql, Object[] countArgs,
                                                        int currentPageNo, int pageSize, Class<T> resultClass) {


        logger.info("queryJqlString : \n {} ", queryJql);
        logger.info("countJqlString : \n {} ", countJql);


        Assert.isTrue(currentPageNo >= 1, "currentPageNo : 起始页不应小于 1 ，且从 1 开始。");
        Assert.isTrue(pageSize >= 0, "pageSize : 页大小不能小于 0");
        Assert.isTrue(countJql.contains("count"), "queryNativeSql 和 countNativeSql 参数顺序不对");

        // 计算总数
        Query queryTotal = em.createQuery(countJql);
        for (int i = 0; i < countArgs.length; i++)
            queryTotal.setParameter(i, countArgs[i]);
        int totalRecordsSize = (int) queryTotal.getSingleResult();


        if (totalRecordsSize == 0)
            return new PageBean(pageSize, 0 + 1, 0, Collections.EMPTY_LIST); //currentPageNo 从 1 开始
//        logger.info("totalRecordsSize : " + totalRecordsSize);
        //分页
        Query query = em.createQuery(queryJql);
        query.setFirstResult((currentPageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
//        final List<Object[]> content = query.getResultList();
//        List<Something> somethingList = Lists.newArrayList();
//        content.forEach(object -> somethingList.add(//map obj to something));

        // 可以代替上述？未测试
        query.unwrap(resultClass); // 返回值包装

        //Bind an argument value to a positional parameter.
        for (int i = 0; i < queryArgs.length; i++)
            query.setParameter(i, queryArgs[i]);
        List<T> content = query.getResultList();

        return new PageBean(pageSize, currentPageNo, totalRecordsSize, content);
    }

    /**
     * 同上，构造查询语句，不分页,自定义返回值包装
     *
     * @param em
     * @param queryJql
     * @param queryArgs
     * @param resultClass
     * @param <T>
     * @return
     */
    private static <T> List<T> queryListByJQLString(final EntityManager em, final String queryJql, Object[] queryArgs, Class<T> resultClass) {


        logger.info("queryJqlString : \n {} ", queryJql);

        Query query = em.createQuery(queryJql);
//        final List<Object[]> content = query.getResultList();
//        List<Something> somethingList = Lists.newArrayList();
//        content.forEach(object -> somethingList.add(//map obj to something));

        // 可以代替上述？未测试
        query.unwrap(resultClass); // 返回值包装

        //Bind an argument value to a positional parameter.
        for (int i = 0; i < queryArgs.length; i++)
            query.setParameter(i, queryArgs[i]);
        List<T> content = query.getResultList();
        return content;
    }

}