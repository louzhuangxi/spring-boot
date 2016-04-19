package com.base.spring.config.multidatasource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
///**
// * 数据源2 ,指定扫描的包即可
// *
// * 修改 三处：
// * 1.EnableJpaRepositories ->basePackages
// * 2.ConfigurationProperties
// * 3. LocalContainerEntityManagerFactoryBean -> packages
// *
// */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = {"com.h819.work.bztx"},//根据实际情况修改
//        entityManagerFactoryRef = "Multi2_EntityManagerFactory",
//        transactionManagerRef = "Multi2_TransactionManager")
//public class Multi2Config {
//
//
//    @Bean(name = "Multi2_DataSource")
//    @ConfigurationProperties(prefix = "spring.oracle.datasource") // 配置文件中，spring.oracle 开头
//    public DataSource barDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "Multi2_EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("Multi2_DataSource") DataSource barDataSource) {
//        return builder
//                .dataSource(barDataSource)
//                .packages("com.h819.work.bztx") //根据实际情况修改
//                .persistenceUnit("Multi2")
//                .build();
//    }
//
//    @Bean(name = "Multi2_TransactionManager")
//    public PlatformTransactionManager barTransactionManager(
//            @Qualifier("Multi2_EntityManagerFactory") EntityManagerFactory barEntityManagerFactory) {
//        return new JpaTransactionManager(barEntityManagerFactory);
//    }
//
//
//}
