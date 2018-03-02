package org.h819.web.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Description : TODO(硬编码，没有用到数据库。)
 * User: h819
 * Date: 2018/2/27
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class MyDataSourceFactory {

    //========================================================
    /**
     * tomcat 配置
     * <Context>
     * <Resource name="jdbc/zjbz"
     * auth="Container"
     * type="javax.sql.DataSource"
     * driverClassName="oracle.jdbc.OracleDriver"
     * url="jdbc:oracle:thin:@129.9.100.11:1521:orcl"
     * username="zjbz"
     * password="zjbz"
     * maxActive="20"
     * maxIdle="10"
     * maxWait="-1"/>
     * </Context>
     */
    //========================================================

    /**
     * tomcat datasource jndi
     * 获取 tomcat 配置文件中设置的数据源
     * https://tomcat.apache.org/tomcat-8.0-doc/jndi-datasource-examples-howto.html#MySQL_DBCP_Example
     *
     * @return
     */
    protected static DataSource getTomcatDataSource() {
        try {
            Context initContext = new InitialContext();
            //Oracle 8i, 9i & 10g 和，其他数据库不同 ，详见上面 Url 说明
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/zjbz");
            return ds;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * apache commons dbcp2 数据源
     *
     * @return
     */
    protected static DataSource getDBCP2DataSource() {
        //创建BasicDataSource类对象
        BasicDataSource datasource = new BasicDataSource();
        //数据库连接信息（必须）
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/mybase");
        datasource.setUsername("root");
        datasource.setPassword("root");
        //连接池中的连接数量配置（可选）
        datasource.setInitialSize(10);//初始化的连接数
        datasource.setMaxOpenPreparedStatements(8);//最大连接数
        datasource.setMaxIdle(5);//最大空闲连接
        datasource.setMinIdle(1);//最小空闲连接
        return datasource;
    }


    /**
     * 根据实际情况，进行数据源切换
     *
     * @return
     */
    protected static DataSource getDataSource() {
        // return getDBCP2DataSource();
        return getTomcatDataSource();
    }


}
