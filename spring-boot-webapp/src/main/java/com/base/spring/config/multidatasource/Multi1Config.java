package com.base.spring.config.multidatasource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
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
// * 数据源1，指定扫描的包即可
// */
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = {"com.h819.work.standard"}, //根据实际情况修改
//        entityManagerFactoryRef = "Multi1_entityManagerFactory",
//        transactionManagerRef = "Multi1_transactionManager")
//public class Multi1Config {
//
//    @Primary
//    @Bean(name = "Multi1_dataSource")
//    @ConfigurationProperties(prefix = "spring.mysql.datasource") // 配置文件中，spring.mysql 开头
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = "Multi1_entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("Multi1_dataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.h819.work.standard")  //根据实际情况修改
//                .persistenceUnit("Multi1")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "Multi1_transactionManager")
//    public PlatformTransactionManager transactionManager(
//            @Qualifier("Multi1_entityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//}
