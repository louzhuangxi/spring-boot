package com.base.oauth2.client.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Description : 生成的 token 和浏览器相关，如果浏览器以 authorization_code 方式访问成功是，如果 AuthorizationServer 设置该 client 同时也支持 password ，那么浏览器也 passwrod 方式方法，就会用同一个 token ，不会在进行验证，直接获得结果
 * User: h819
 * Date: 2015/12/4
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableOAuth2Client
@Transactional
public class Oauth2ClientRestTemplate {

    private static final Logger logger = LoggerFactory.getLogger(Oauth2ClientRestTemplate.class);

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    /**
     *
     * === oauth 2.0
     *  是一种安全验证协议，有很多实现其 oauth 协议的项目，如 spring security oauth2
     *  其授权类型(grant_type)有四种，分布是 authorization_code , implicit , password , client_credentials
     *
     *  主要验证过程：通过身份验证以后，获得 access_token ，access_token 是获得授权的标志。access_token 有有效期，过了有效期，需要重新获取 access_token。
     *
     *  1. authorization_code (或 implicit ，不常用):
     *
     *     第三方网站提供用户身份验证服务。
     *     在 client 在不知道用户名和密码的情况下，通过自己登陆到第三方网站，client 在第三方网站登录成功后，获取到 access_token ，表明已通过身份验证，之后再访问资源服务器内容。
     *     目前这种认证方式是 oauth 的主要用途，如各个网站提供的单点登录功能，就是利用第三方提供的 oauth 的认证，让用户通过第三方的账号，登陆本网站，而网站本身不保留用户的信息。
     *
     *  2. password 和 client_credentials 方式:
     *
     *  2.1  password :
     *     知道了用户名和密码，通过登录 AuthorizationServer 获得 access_token 后，通过 access_token 访问 ResourceServer 资源；
     *
     *  2.2  client_credentials :
     *     client 知道资源的 client_id (也可以是 client_id 和 client_secret），获得 access_token 后，通过access_token 访问资源。
     *
     *     对于这两种种情况，都需要用户向验证服务器提供身份信息，获得 access_token 后，在 access_token 的有效期内，不用再提供身份信息，通过 access_token ，访问资源。
     *
     *  3. 关于信息传递安全
     *     oauth 验证分为两个步骤
     *     1）身份验证，获得 access_token
     *     2）利用 access_token 访问资源服务器（此时不再需要身份信息，access_token 过期后，重新获取）
     *
     *     为了防止传递的信息被截获，
     *     步骤 1） 需要通过 https 协议进行数据传输。https 协议是加密协议，传递的用户身份信息（用户名、密码等）不会被截获。
     *     完成了 1）的身份验证以后，为了提供效率， 2）步骤可以通过 http 协议 ，这是目前大多数网站采取的策略。（2）步骤也可以用 https）
     *
     *  4. 关于开放 API 的安全验证
     *
     *  4.1 采用上文提到的 oauth 方式验证
     *      grant_type=client_credentials 的方式
     *      此种方式，服务器端需要搭建验证服务器(ssh 协议模式)和资源服务器，客户端需要 oauth 客户端技术进行访问。
     *
     *  4.2 利用 HMAC（Hash-based Message Authentication Code 基于散列的消息认证码）
     *
     *
     *
     *  5. 关于生成的 access_token
     *  四种方式生成的 token 和该 client 的相关信息相关:
     *  如用一台电脑的同一浏览器登陆 AuthorizationServer 后，验证成功以后，该电脑的这个浏览器无论以哪种方式，在 token 有效期内不必登陆，直接返回验证成功信息。
     *  登陆一次之后不需要再次登陆，是因为第一次登陆的 token 还没有过期，所以可以继续自动登陆。

     *  如果出现有时验证成功，有时不成功的情况，大多数是因为更改了相关参数，此时删除以后的  token 信息，重新启动电脑即可。
     *
     *  常用的有两种模式
     *  grant_type=client_credentials ，用于对外开放 api
     *  grant_type=authorization_code ，用于单点登录
     *
     *
     *  === oauth2 四种验证方式说明和应用场景 ===
     * 1.  grant_type=authorization_code (重点，常用)
     *
     * 这种验证方式，需要 client 到验证服务器登录页面进行登陆，登陆之后验证服务器保存该 client 的 token
     * -
     * 有的网站引入了第三方登陆方式，登陆一次之后不需要再次登陆，是因为第一次登陆的 token 还没有过期，所以可以继续自动登陆。
     * -
     * 实际应用场景：
     * 单点登录： 不同的第三方应用，用验证服务器统一进行验证登陆，登陆成功之后，返回一个登陆成的 access_token ，表示该用户存在
     * 那么登陆成功后，该 client 的权限信息保存在哪里呢，每个用户的权限不一样，如有的仅有读权限，有的有写权限 ？登陆成功仅能证明该用户存在。
     * 可以有两种模式：
     * - 1) 用户的权限信息保存在验证服务器，可以在登陆成功时，直接返回该用户的权限信息。缺点是，验证服务器上要根据不同的应用，设置不同的用户权限，到底是管理员还是普通用户
     * - 2) 权限信息保存在第三方应用上，这样验证服务器只保证用户存在，不管该用户有什么权限。
     * - 现在看来，应该是 2) 方式，要不然 google 提供的登陆，没办法给那么多的第三方应用分别设置权限了。
     * - 例子：google 开放的登陆，网易新闻保存到有道云笔记等，都会出现一个授权页面，就是该方式的例子。
     * -

     * ------------------------------------------------------------------------------------------------------------------------
     * 2.  grant_type=implicit
     *   该方式没有实验成功,据说是用在 js 中 。
     *   不再尝试该方法，等需要时再看是为什么(google 了半个月的资料，不再试了)。
     *
     * ------------------------------------------------------------------------------------------------------------------------
     *  3.  grant_type=password
     *  该方式需要提供在验证服务器上存在的用户名和密码（如果验证服务器上设置了 client 用户角色信息，则该用户还应该满足角色要求）
     *
     *------------------------------------------------------------------------------------------------------------------------
     *  4.  grant_type=client_credentials (重点，常用)
     *  用于 web 应用向第三方应用提供 api 接口，此时应设置 client_secret ，增加安全性。
     *  典型用例 :
     *  微信 -
     *  微信对公众号提供的接口，就是用了 grant_type=client_credentials 的方式。第三方应用 通过 access_token 来获取相关信息，access_token 的有效期是两个小时(见“微信公众平台开发者文档 / 获取接口凭证 http://mp.weixin.qq.com/wiki/14/9f9c82c1af308e3b14ba9b973f99a8ba.html”)。
     *  对外提供的资源用 get 方法。
     *  不知道微信用的是不是 ouath2 协议，因为 spring ouath2 的 client_credentials 不提供 refresh_token ，而微信的公众号接口提到可以有。
     *  - 所以如果想向外提供 api ，就用此方式。
     *  - 对于每个对外的用户，都单独设置一个 client_id 和 client_secret ，微信的方案是每个用户都有一个。相应限制用户的连接次数，在接收到其发送的查询请求的时候处理，如果通过，在转发到 oauth server。
     */

    /**
     * 演示 grant_type=authorization_code 时，获取资源的方法
     * -
     *
     * @param client_id
     * @param client_secret     取决于 AuthorizationServer 设置，如果 client 设置了secret，则此项参数为必需，否则可以没有
     * @param access_token_uri
     * @param authorization_uri
     * @param scope
     * @return
     */

    public OAuth2RestOperations authorizationCodeRestTemplate(String client_id, String client_secret, String authorization_uri, String access_token_uri, String... scope) {

        // 防止 url 写错
        if (!access_token_uri.contains("token") || !authorization_uri.contains("authorize"))
            throw new RuntimeException("uri is wrong :  access_token_uri = " + access_token_uri + " , authorization_uri" + authorization_uri);


        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setId("1");
        details.setClientId(client_id);
        if (client_secret != null && !client_secret.isEmpty())
            details.setClientSecret(client_secret);
        details.setAccessTokenUri(access_token_uri);
        details.setUserAuthorizationUri(authorization_uri);
        details.setUseCurrentUri(true); //将当前请求的 uri 作为参数 redirect_uri 接受返回值。设置为 faslse 是，需要设置 redirect_uri 参数, details.setPreEstablishedRedirectUri("http://anywhere");
        details.setScope(Arrays.asList(scope));
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }

    /**
     * 该方式没有实验成功，设置为 Deprecated!
     * <p>
     * 演示 grant_type=implicit 时，获取资源的方法
     *
     * @param client_id
     * @param client_secret     取决于 AuthorizationServer 设置，如果 client 设置了secret，则此项参数为必需，否则可以没有
     * @param authorization_uri
     * @param access_token_uri
     * @param scope
     * @return
     */
    @Deprecated
    public OAuth2RestOperations implicitResourceRestTemplate(String client_id, String client_secret, String authorization_uri, String access_token_uri, String... scope) {

        // 防止 url 写错
        if (!authorization_uri.contains("authorize"))
            throw new RuntimeException("uri is wrong :  authorization_uri" + authorization_uri);

        ImplicitResourceDetails details = new ImplicitResourceDetails();
        details.setId("2");
        details.setClientId(client_id);
        if (client_secret != null && !client_secret.isEmpty())
            details.setClientSecret(client_secret);
        details.setAccessTokenUri(authorization_uri);
        details.setClientAuthenticationScheme(AuthenticationScheme.header);
        details.setUseCurrentUri(true);
        details.setScope(Arrays.asList(scope));
        // return restTemplate;
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }

    /**
     * 演示 grant_type=password 时，获取资源的方法
     * 用的场景还不知道，@Deprecated
     *
     * @param client_id
     * @param client_secret    取决于 AuthorizationServer 设置，如果 client 设置了secret，则此项参数为必需，否则可以没有
     * @param access_token_uri
     * @param username
     * @param password
     * @param scope
     * @return
     */
    @Deprecated
    public OAuth2RestOperations resourceOwnerPasswordRestTemplate(String client_id, String client_secret, String access_token_uri, String username, String password, String... scope) {

        // 防止 url 写错
        if (!access_token_uri.contains("token"))
            throw new RuntimeException("uri is wrong :  access_token_uri = " + access_token_uri);

        // 防止 client_secret 写错
        if (username == null || password == null || username.isEmpty() || password.isEmpty())
            throw new RuntimeException("username or password  is wrong :  username or password is a required parameter");

        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setId("3");
        details.setClientId(client_id);
        if (client_secret != null && !client_secret.isEmpty())
            details.setClientSecret(client_secret);
        details.setAccessTokenUri(access_token_uri);
        details.setUsername(username);
        details.setPassword(password);
        details.setScope(Arrays.asList(scope));
        return new OAuth2RestTemplate(details, oAuth2ClientContext);


    }

    /**
     * 演示 grant_type=client_credentials 时，获取资源的方法
     *
     * @param client_id
     * @param client_secret    取决于 AuthorizationServer 设置，如果 client 设置了secret，则此项参数为必需，否则可以没有
     * @param access_token_uri
     * @param scope
     * @return
     */
    public OAuth2RestOperations clientCredentialsRestTemplate(String client_id, String client_secret, String access_token_uri, String... scope) {

        // 防止 url 写错
        if (!access_token_uri.contains("token"))
            throw new RuntimeException("uri is wrong :  access_token_uri = " + access_token_uri);

        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId("4");
        details.setClientId(client_id);
        if (client_secret != null && !client_secret.isEmpty())
            details.setClientSecret(client_secret);
        details.setAccessTokenUri(access_token_uri);
        details.setScope(Arrays.asList(scope));
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }


    private OAuth2RestOperations refreshRestTemplate(String resource) {

        //https://github.com/spring-projects/spring-security-oauth/blob/master/samples/oauth2/tonr/src/test/java/org/springframework/security/oauth/examples/tonr/RefreshTokenGrantTests.java
        // 如果 client 设置了可以 refresh ，那么上述四种获取方法中，就会自动利用 refresh 的方式获取资源，不必单独实现

        return null;
    }

}