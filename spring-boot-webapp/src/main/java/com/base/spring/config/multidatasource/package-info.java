/**
 * 详见 open api server 项目
 * ---
 * 单数据源不用配置此类
 * ----
 * 多数据源配置 步骤 :
 * --- 必须保证两个数据源都能连通
 * 配置 MultiConfig1 , MultiConfig2 即可
 * 1. application-multi.properties 文件中正确命名
 * # 注意，最后一个点后面的为关键字，如 url,username,password.
 * # mysql 为自定义的前缀
 * -
 * 2. 配置两个数据源配置文件，MySQLDataSourceConfig.java  和 OracleDataSourceConfig
 * -
 * 3.数据源相关的配置
 *
 * @EnableJpaRepositories( basePackages = {"com.open.api.mysql"} ...
 * basePackages 指定了该数据源对应各个配置，如  domain(@Entity),@repository,@controller ,@service , @Transactional 默认的名字等
 * 这些配置都会到 basePackages 找对应的各个类
 * 所以为了便于对应，每个数据源对应的  domain(@Entity),@repository,@controller ,@service 都应放在一个包中，便于管理。
 * -
 * 4. 只有一个数据源需要设定优先级 @Primary ，另外一个不设
 * -
 * 5. 应用时，和单数据源一样，不再有其他设置，因为不同的 basePackages ，对应不同的数据源，会自动对应好
 * -
 * 6. 在 properties 中，设置正确的 jpa.database-platform，否则会在 @Test 是发生错误
 * -
 * 7. 在应用测试类的时候， @Transactional 无法和数据源对应，需要指定测试用到的是哪个数据源
 * 在方法级上设置即可，不同的方法可以用不同的数据源
 * @Test
 * @Transactional(value ="mysqlTransactionManager") -
 * -
 * 8. 如果在同一个方法中用到了不同的数据源，可以改写成两个方法，之后分别设置 Transactional ，见 MultiDataSourceExample
 * Time: 2016.03.29 :16:47
 */
package com.base.spring.config.multidatasource;