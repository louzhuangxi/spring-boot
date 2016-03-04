package com.base.oauth2.client.template;

import com.alibaba.fastjson.JSON;
import com.base.SpringOauth2ClientApplication;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/10
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringOauth2ClientApplication.class)
@WebAppConfiguration
public class Oauth2ClientRestTemplateTest {

    @Value("unity-client")
    private String client_id;

    @Value("unity")
    private String client_secret;

    @Value("http://localhost:8080/spring-oauth-server/unity/user_info")
    private String source_url;

    @Value("http://localhost:8080/spring-oauth-server/oauth/token")
    private String access_token_uri;

    @Value("http://localhost:8080/spring-oauth-server/oauth/authorize")
    private String user_authorization_uri;

    @Value("unity")
    private String username;

    @Value("unity")
    private String password;

    private String[] scopes = new String[]{"read"};

    @Autowired
    private Oauth2ClientRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAuthorizationCodeRestTemplate() throws Exception {
        OAuth2RestOperations operations = restTemplate.authorizationCodeRestTemplate(client_id, client_secret, access_token_uri, user_authorization_uri, scopes);
        System.out.println(JSON.toJSONString(operations.getForObject(source_url, JsonNode.class)));  // getForObject 发送 get 方法
    }

    @Test
    public void testImplicitResourceRestTemplate() throws Exception {

    }

    @Test
    public void testResourceOwnerPasswordRestTemplate() throws Exception {

        OAuth2RestOperations operations = restTemplate.resourceOwnerPasswordRestTemplate(client_id, client_secret, access_token_uri, username, password, scopes);  // getForObject 发送 get 方法
        System.out.println(JSON.toJSONString(operations.getForObject(source_url, JsonNode.class)));  // getForObject 发送 get 方法
    }

    @Test
    public void testClientCredentialsRestTemplate() throws Exception {

        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
         details.setId("4");
        details.setClientId(client_id);
        details.setClientSecret(client_secret);
        details.setAccessTokenUri(access_token_uri);
       // details.setScope(Arrays.asList("read write"));
        OAuth2RestTemplate operations = new OAuth2RestTemplate(details,new DefaultOAuth2ClientContext());
       // OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
        operations.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());

      //  OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails(),oAuth2ClientContext());
        DefaultOAuth2AccessToken token=(DefaultOAuth2AccessToken)operations.getAccessToken();
        token.setTokenType("Bearer");

        System.out.println("client_id : " + client_id);
        System.out.println("source_url : " + source_url);

      //  OAuth2RestOperations operations = restTemplate.clientCredentialsRestTemplate(client_id, client_secret, access_token_uri, scopes);  // getForObject 发送 get 方法
        System.out.println(JSON.toJSONString(operations.getForObject(source_url, JsonNode.class)));  // getForObject 发送 get 方法

    }
}