package org.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:persistence.properties"})
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("authenticationManagerBean") // WebSecurityConfig 定义
    private AuthenticationManager authenticationManager;

    @Value("classpath:schema.sql")
    private Resource schemaScript;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * 授权服务器响应的的客户端配置。
     *
     * @param clients
     * @return
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        /**
         *  由于 AuthorizationServer 和 ResourceServer 分开设置，为了贡献 token ，只能用 JdbcTokenStore 方式。
         *  此种方式，在系统启动时，会把此处的 client 配置信息，直接存入数据库
         *
         *  如果有很多个 client 需要管理，则需要把 client 存储在数据库中。需要自己实现
         *  此处不启用该特性
         *  ( 保存在数据库中，可以编辑 : clients.withClientDetails(clientDetailsService);)
         */

        /**
         * 注册 client_id 时，都必须指定其拥有的 resource-id（只能指定一个），而 resource-id 又指定了具体资源，故每个 client_id 只能访问特定的资源
         * 可以注册多个  client_id
         */

        /**
         *  .authorities() ：
         *
         *  如果获取资源的时候，验证角色信息，那么该参数应该设置，表示该 client 的角色
         *  如 @PreAuthorize("#oauth2.hasScope('read') and hasRole('USER')")
         *  此时需要设置该 client 的  .authorities("USER")
         *
         *  对于对外提供 api 时，为了限制其他用户的方法，还是加上测方法为好
         *
         * 1.  authorizedGrantTypes 为 authorization_code,password 时，authorities 参数可以不设置,
         *     因为 authorization_code 方式需要登陆，password 方式需要 username 参数，所以系统可以根据 username 获取 authorities
         * 2.authorizedGrantTypes 为 implicit,client_credentials 时，authorities 参数必须设置
         *
         */

        /**
         *  .autoApprove() :
         *  设置用户是否自动Approval操作, 默认值为 'false', 可选值包括 'true','false', 'read','write'.
         *  该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为'true'或支持的scope值,则会跳过用户Approve的页面(该页面可以自定义), 直接授权.
         *  该字段与 trusted 有类似的功能, 是 spring-security-oauth2 的 2.0 版本后添加的新属性.
         *
         */

        /**
         *  refresh_token :不能单独存在，需要和其他的类型一起，表示该类型的 access_token 可以 刷新
         *  authorization_code 和 password 的 token 返回值可以有 refresh_token ，"implicit", "client_credentials" 方式为不可信模式，access_token 不能刷新，所以不必设置 refresh_token 参数
         */

        /**
         *  .secret :各个登陆方式都可以有次参数，如果  AuthorizationServer 端设置了，那么 client 端调用必需包含此参数，如果 AuthorizationServer 端没有设置，则 client 端也不能有此参数
         */

        /**
         *  scopes("read") : 该参数不代表什么含义，只是在安全验证的时候有用
         *   如 @PreAuthorize("#oauth2.hasScope('read') and hasRole('USER')")
         *   此时应该设置  .scopes("read");
         */

        /**
         * 根据 Oauth2ClientRestTemplate.java 的分析，常用的设置有两个
         * grant_type=client_credentials ，用于对外开放 api
         * grant_type=authorization_code ，用于单点登录
         *
         * 对每个用户，设置单独的 client 和 secret
         *
         *
         */

        // @formatter:off
        clients.jdbc(dataSource())
                .withClient("type1")
                .secret("secret1")
                .authorizedGrantTypes(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN) // 用于单点登录
                .scopes("read")
                        // .authorities("USER")
                        // .accessTokenValiditySeconds(60)
                .autoApprove(false)
                .and()

                .withClient("type2")   // 用于对外开放 api
                .secret("secret2")
                .authorizedGrantTypes(GrantType.CLIENT_CREDENTIALS) //不能有刷新功能
                .authorities("TYPE2_USER") //对于对外提供 api 时，为了限制其他用户的方法，在方法上加上  @PreAuthorize("#oauth2.hasScope('read') and hasRole('TYPE2_USER')") , 以表示只有该 角色可以访问
                .scopes("read"); //如果需要验证，则加此方法

        // @formatter:on
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        return dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource());
    }
}
