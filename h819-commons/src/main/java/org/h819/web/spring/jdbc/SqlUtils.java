package org.h819.web.spring.jdbc;

import java.text.MessageFormat;


/**
 * Sql 工具类，标准 jdk ，不引入第三方 lib
 * 需要注意的是 MessageFormat.format 会去掉 ''
 * 所以 sql 语句需要以参数形式传入，不能直接连接，见 createSqlString 方法注释
 */

public class SqlUtils {
    /**
     * 分页SQL，仅测试了 oracle
     * 通过数据库分页，在满足条件的所有结果中截取一夜数据，所以传入的查询参数 {0} 是不分页时候的查询条件
     */
    private static final String MYSQL_SQL = "select * from ({0}) sel_tab00 limit {1},{2}"; // mysql
    private static final String POSTGRE_SQL = "select * from ({0}) sel_tab00 limit {2} offset {1}";// postgresql
    // oracle ，没有根据 oracle 版本进行优化
    private static final String ORACLE_SQL = "select * from (\n" +
            "  select rownum row_num, subq.* \n" +
            "  from \n" +
            "    ({0}) subq\n" +
            "     where rownum <= {1}) \n" +
            "where row_num > {2}"; // oracle

    private static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver

    /**
     * 构造数据库相关的本地分页查询语句
     * -
     * 排序条件不在这里维护
     *
     * @param dbDialect      数据库类型，数据库不同，分页语句不同
     * @param queryNativeSql 本地查询条件，和不分页时相同，之后在所有的数据中截取一页，如
     *                       select * from standard st where st.namecn like '%中国%' ,
     *                       select st.id,st.name from standard st where st.id ='239711'
     *                       需要注意的是，不要包含排序条件，如果有排序条件，作为下面的参数传入
     * @param currentPageNo  当前页码，从 1 开始
     * @param pageSize       页大小
     * @return
     */
    public static String createNativePageSqlString(Dialect dbDialect,
                                                   String queryNativeSql,
                                                   int currentPageNo, int pageSize) {

        if (currentPageNo < 1)
            throw new IllegalArgumentException("currentPageNo : 起始页应从 1 开始。");

        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize : 页大小不能小于 0");

        String[] sqlParam = new String[3];
        int beginIndex = (currentPageNo - 1) * pageSize; //sql 语句，起始页，从 0 开始
        sqlParam[0] = queryNativeSql;
        sqlParam[1] = beginIndex + "";
        sqlParam[2] = pageSize + "";

        if (dbDialect.equals(Dialect.MySql)) { //mysql
            queryNativeSql = MessageFormat.format(MYSQL_SQL, sqlParam);
        } else if (dbDialect.equals(Dialect.PostgreSQL)) { // postgre
            queryNativeSql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        } else {

            //计算起止
            int endIndex = beginIndex + pageSize;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);

            //oracle
            if (dbDialect.equals(Dialect.Oracle)) { // oracle
                queryNativeSql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if (dbDialect.equals(Dialect.SqlServer)) {//SqlServer
                sqlParam[0] = queryNativeSql.substring(getAfterSelectInsertPoint(queryNativeSql));
                queryNativeSql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return queryNativeSql.trim();
    }


    /**
     * 构造 sql server 分页语句时使用
     *
     * @param sql
     * @return
     */
    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }


    public static void main(String[] args) throws Exception {
        String searchSql = "select ccs.ccs_name as ccsName,ics.ics_name as icsName,cl.class_name_cn as className, st.* from St_Standard st\n" +
                "left join st_ccs_class ccs on st.ccs_code = ccs.ccs_code \n" +
                "left join st_ics_class ics on st.ics_code = ics.ics_code\n" +
                "left join st_class cl on  (st.class_three_code = cl.class_code and cl.class_layer =3)   \n" +
                "where st.standard_code like '%2760%'";

        //配合 searchSql ，不用 left join ，因为不需要 left table ,结果是一样的
        String countSql = "select count(*) from St_Standard st " +
                "where st.standard_code like '%2760%'";

        int currentPageNo = 1;
        int pageSize = 10;
        System.out.println("MYSQL : \n" + SqlUtils.createNativePageSqlString(Dialect.MySql, searchSql, currentPageNo, pageSize));
        System.out.println("ORACLE :\n" + SqlUtils.createNativePageSqlString(Dialect.Oracle, searchSql, currentPageNo, pageSize));
        System.out.println("SQLSERVER : \n" + SqlUtils.createNativePageSqlString(Dialect.SqlServer, searchSql, currentPageNo, pageSize));
        System.out.println("POSTGRE :\n" + SqlUtils.createNativePageSqlString(Dialect.PostgreSQL, searchSql, currentPageNo, pageSize));
    }


    /**
     * 数据库类型
     */
    public enum Dialect {

        MySql,
        Oracle,
        SqlServer,
        PostgreSQL;
    }

    /**
     * 数据库排序，为了可以脱离 spring 使用 ，不使用 org.springframework.data.domain.Sort
     */
    public enum SortDirection {
        DESC, ASC
    }
}
