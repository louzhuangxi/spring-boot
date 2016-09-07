package org.h819.web.spring.jdbc;

/**
 * Sql 工具类，标准 jdk ，不引入第三方 lib
 * 需要注意的是 MessageFormat.format 会去掉 ''
 * 所以 sql 语句需要以参数形式传入，不能直接连接，见 createSqlString 方法注释
 */

import java.text.MessageFormat;
import java.util.Arrays;

public class SqlUtils {
    /**
     * 分页SQL，仅测试了 oracle
     */
    private static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}"; // mysql
    private static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";// postgresql
    // oracle ，没有根据 oracle 版本进行优化
    private static final String ORACLE_SQL = "select * from (\n" +
            "  select rownum row_num, subq.* \n" +
            "  from \n" +
            "    ({0} {3} {4}) subq\n" +
            "     where rownum <= {1}) \n" +
            "where row_num > {2}"; // oracle

    private static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver

    /**
     * 构造数据库相关的本地分页查询语句
     *
     * @param dbDialect       数据库类型，数据库不同，分页语句不同
     * @param queryNativeSql  本地查询条件，和不分页时相同，如 select * from standard st where st.namecn like '%中国%' , select st.id,st.name from standard st where st.id ='239711'
     *                        需要注意的是，不要包含排序条件，如果有排序条件，作为下面的参数传入
     * @param currentPageNo   当前页码，从 1 开始
     * @param pageSize        页大小
     * @param searchParameter 排序参数，如果不排序，设置为 null
     * @param sort            dese or asc  ，仅提供一种，其他例子同理
     * @return
     */
    public static String createNativePageSqlString(Dialect dbDialect, String queryNativeSql, int currentPageNo, int pageSize, String[] searchParameter, SortDirection sort) {

        if (currentPageNo < 1)
            throw new IllegalArgumentException("currentPageNo : 起始页应从 1 开始。");

        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize : 页大小不能小于 0");

        String[] sqlParam = new String[5];
        int beginIndex = (currentPageNo - 1) * pageSize; //sql 语句，起始页，从 0 开始
        sqlParam[0] = queryNativeSql;
        sqlParam[1] = beginIndex + "";
        sqlParam[2] = pageSize + "";

        //有无排序条件
        if (searchParameter == null || sort == null) {
            sqlParam[3] = "";
            sqlParam[4] = "";
        } else {
            //构造排序条件语句
            sqlParam[3] = " order by  " + Arrays.toString(searchParameter).replace("[", "").replace("]", "") + "";
            sqlParam[4] = sort + "";
        }


        if (dbDialect.equals(Dialect.MySql)) { //mysql
            queryNativeSql = MessageFormat.format(MYSQL_SQL, sqlParam);
        } else if (dbDialect.equals(Dialect.PostgreSQL)) { // postgre
            queryNativeSql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        } else {

            //计算起止
            int endIndex = beginIndex + pageSize;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);

            if (dbDialect.equals(Dialect.Oracle)) { // oracle
                queryNativeSql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if (dbDialect.equals(Dialect.SqlServer)) {
                sqlParam[0] = queryNativeSql.substring(getAfterSelectInsertPoint(queryNativeSql));
                queryNativeSql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return queryNativeSql.trim();
    }


    /**
     * 同上，无排序条件
     *
     * @param dbDialect
     * @param queryNativeSql
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    public static String createNativePageSqlString(Dialect dbDialect, String queryNativeSql, int currentPageNo, int pageSize) {
        return createNativePageSqlString(dbDialect, queryNativeSql, currentPageNo, pageSize, null, null);
    }

    /**
     * 为查询语句添加排序参数
     *
     * @param queryNativeSql
     * @param searchParameter
     * @param sort
     * @return
     */
    public static String createSqlString(String queryNativeSql, String[] searchParameter, SortDirection sort) {
        String[] sqlParam = new String[3];
        sqlParam[0] = queryNativeSql;
        //有无排序条件
        if (searchParameter == null || sort == null) {
            sqlParam[1] = "";
            sqlParam[2] = "";
        } else {
            //构造排序条件语句
            sqlParam[1] = " order by  " + Arrays.toString(searchParameter).replace("[", "").replace("]", "") + "";
            sqlParam[2] = sort + "";
        }

        // queryNativeSql = MessageFormat.format(queryNativeSql + "{1} {2}", sqlParam);此种方法，会过滤掉 queryNativeSql 中的 '' ，所以 queryNativeSql 需要以参数形式传入 {0}
        queryNativeSql = MessageFormat.format("{0} {1} {2}", sqlParam);
//        System.out.println("queryNativeSql : \n" + queryNativeSql);
        return queryNativeSql.trim();
    }


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
        //  System.out.println("MYSQL : \n" + SqlUtils.createNativePageSqlString(DBDialect.MySql, searchSql, currentPageNo, pageSize, null, null));
        //  System.out.println("ORACLE :\n" + SqlUtils.createNativePageSqlString(DBDialect.Oracle, searchSql, currentPageNo, pageSize, new String[]{"id", "namecn"}, DBSort.DESC));
        // System.out.println("SQLSERVER : \m" + SqlUtils.createNativePageSqlString(DBDialect.SqlServer, searchSql, currentPageNo, pageSize, null, null));
        // System.out.println("POSTGRE :\n" + SqlUtils.createNativePageSqlString(DBDialect.PostgreSQL, searchSql, currentPageNo, pageSize, null, null));

        String[] ss = {};
//        System.out.println(Arrays.toString(ss));
        System.out.println(Arrays.toString(ss).replace("[", "").replace("]", ""));

        //  System.out.println("\n" + createSqlString(searchSql, new String[]{"id", "namecn"}, DBSort.ASC));

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
