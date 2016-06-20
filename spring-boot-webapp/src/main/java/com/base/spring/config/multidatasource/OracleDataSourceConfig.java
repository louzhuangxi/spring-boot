package com.base.spring.config.multidatasource;

/**
 * oracle
 */
     /*
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.open.api.oracle"},
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager")         */
public class OracleDataSourceConfig {    /*

    @Primary
    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "oracle.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("oracleDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.open.api.oracle")
                .persistenceUnit("oracle")
                .build();
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    */
}
