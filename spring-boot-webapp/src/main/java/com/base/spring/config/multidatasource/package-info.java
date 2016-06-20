/**
 * 多数据源配置 步骤 :
 * 配置 MultiConfig1 , MultiConfig2 即可
 * 1. application-multi.properties 文件中正确命名， datasource 前只能有一个单词，系统根据 datasource 关键字配置信息
 * -
 * 2. 配置两个数据源配置文件，MySQLDataSourceConfig.java  和 OracleDataSourceConfig
 * -
 * 3.每个数据源创建的 domain(@Entity),@repository,@controller ,@service 类都应放在一个包中，
 * datasource config 文件扫描的时候，需要配置扫描的 entity 和 repository 包，放在一个文件夹中为好
 * -
 * 4. 只有一个数据源需要设定优先级 @Primary ，另外一个不设
 * -
 * 5. 应用时，和单数据源一样，不在有其他设置
 * Time: 2016.03.29 :16:47
 */
package com.base.spring.config.multidatasource;