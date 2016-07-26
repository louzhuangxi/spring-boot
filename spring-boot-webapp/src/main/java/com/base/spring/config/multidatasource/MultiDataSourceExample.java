package com.base.spring.config.multidatasource;

/**
 * Description : TODO(演示在一个方法中，使用两个数据源)
 * User: h819
 * Date: 2016/7/12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class MultiDataSourceExample {

    /**
     * 演示在一个方法中，使用两个数据源 ，需要在 properties 中，分别为两个数据源设置正确的 jpa.database-platform 属性，否则会在 @Test 是发生错误
     * mysql.datasource.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
     * oracle.datasource.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect
     * <p>
     * 执行 test1 方法即可
     *
     * @throws Exception


  @Transactional(value = "oracleTransactionManager", readOnly = true)
    private void test1() throws Exception {      //  oracle

        //do something on oracle datasource

        // also do on mysql datasource
        test2();

    }

    // 切换到mysql
    @Transactional(value = "mysqlTransactionManager", readOnly = true)
    private void test2() {

        //do something on mysql datasource

    } */
}
