package com.base.spring.config.multidatasource;

/**
 * mysql
 */
    /*
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.open.api.mysql"},
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlTransactionManager")     */
public class MySQLDataSourceConfig {
                                       /*
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "mysql")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder build,
            @Qualifier("mysqlDataSource") DataSource dataSource) {
        return build
                .dataSource(dataSource)
                .packages("com.open.api.mysql")
                .persistenceUnit("mysql")
                .build();
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
       */
}
